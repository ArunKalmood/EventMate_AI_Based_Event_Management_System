package com.springboard.eventmate.smart.rules;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.Notification;
import com.springboard.eventmate.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DemandNotificationService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final NotificationRepository notificationRepository;

	private static final String KEY_PREFIX = "demand:state:event:";

	public void processDemandTransition(
	        Event event,
	        DemandClassificationResult result
	) {

	    if (event == null || result == null || result.getDemandState() == null) {
	        return;
	    }

	    String key = KEY_PREFIX + event.getId();

	    DemandState previousState = null;

	    try {
	        previousState =
	            (DemandState) redisTemplate.opsForValue().get(key);

	        // Store latest state
	        redisTemplate.opsForValue().set(key, result.getDemandState());

	    } catch (Exception e) {
	        // Redis is down → skip demand tracking safely
	        log.warn(
	            "Redis unavailable for demand tracking, eventId={}",
	            event.getId()
	        );
	        return;
	    }

	    DemandState currentState = result.getDemandState();

	    if (previousState == null) {
	        return;
	    }

	    if (previousState == DemandState.NONE &&
	        currentState == DemandState.TRENDING) {

	        createNotification(event, "Event is now TRENDING");
	    }

	    if (previousState == DemandState.TRENDING &&
	        currentState == DemandState.FILLING_FAST) {

	        createNotification(event, "Event is FILLING FAST");
	    }
	}


	private void createNotification(Event event, String title) {

		boolean alreadyNotified =
				notificationRepository.existsByUserIdAndEventIdAndTitle(
						event.getCreatedBy().getId(),
						event.getId(),
						title
						);

		if (alreadyNotified) {
			return;
		}

		Notification notification = new Notification();
		notification.setUser(event.getCreatedBy());
		notification.setEventId(event.getId());
		notification.setLostItemId(0L); // Not used here
		notification.setTitle(title);
		notification.setMessage(
				"Demand for your event \"" + event.getTitle() + "\" has increased."
				);

		try {
			notificationRepository.save(notification);
		} catch (Exception e) {
			log.warn(
					"Demand notification failed for eventId={}, title={}",
					event.getId(),
					title,
					e
					);
		}
	}
}
