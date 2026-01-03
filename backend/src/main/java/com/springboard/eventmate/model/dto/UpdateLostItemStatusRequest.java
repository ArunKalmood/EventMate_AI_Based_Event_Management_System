package com.springboard.eventmate.model.dto;

import com.springboard.eventmate.model.enums.LostItemStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLostItemStatusRequest {

    @NotNull
    private LostItemStatus status;
}
