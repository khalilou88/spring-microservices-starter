package com.example.userapi.event;

import com.example.core.messaging.event.BaseEvent;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDeletedEvent extends BaseEvent {

    @JsonProperty("userId")
    private final Long userId;

    @JsonProperty("userEmail")
    private final String userEmail;

    public UserDeletedEvent(Long userId, String userEmail) {
        this.userId = userId;
        this.userEmail = userEmail;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
