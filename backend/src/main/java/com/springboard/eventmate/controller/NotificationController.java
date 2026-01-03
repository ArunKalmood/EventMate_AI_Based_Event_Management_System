package com.springboard.eventmate.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboard.eventmate.model.dto.NotificationResponseDTO;
import com.springboard.eventmate.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping
	public ResponseEntity<Page<NotificationResponseDTO>> getMyNotifications(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
			) {
		return ResponseEntity.ok(
				notificationService.getMyNotifications(page, size)
				);
	}

	@GetMapping("/unread")
	public ResponseEntity<Page<NotificationResponseDTO>> getMyUnreadNotifications(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
			) {
		return ResponseEntity.ok(
				notificationService.getMyUnreadNotifications(page, size)
				);
	}

	// =========================
	// EV-416 — Mark single notification as read
	// =========================
	@PutMapping("/{notificationId}/read")
	public ResponseEntity<Void> markAsRead(
			@PathVariable Long notificationId
			) {
		notificationService.markNotificationAsRead(notificationId);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/read-all")
	public ResponseEntity<Void> markAllAsRead() {
	    notificationService.markAllAsRead();
	    return ResponseEntity.ok().build();
	}
	
	

}
