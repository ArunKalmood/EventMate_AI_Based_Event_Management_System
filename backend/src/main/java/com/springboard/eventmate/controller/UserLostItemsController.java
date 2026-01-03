package com.springboard.eventmate.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboard.eventmate.model.dto.LostItemResponse;
import com.springboard.eventmate.service.LostItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class UserLostItemsController {

    private final LostItemService lostItemService;

    @GetMapping("/lost-items")
    public ResponseEntity<List<LostItemResponse>> getMyLostItems() {
        return ResponseEntity.ok(
                lostItemService.getLostItemsForCurrentUser()
        );
    }
}
