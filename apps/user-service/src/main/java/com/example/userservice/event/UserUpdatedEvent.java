package com.example.userservice.event;

import com.example.core.messaging.event.BaseEvent;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserUpdatedEvent extends BaseEvent {

    @JsonProperty("userId")
    private final Long userId;

    @JsonProperty("userName")
    private final String userName;

    @JsonProperty("userEmail")
    private final String userEmail;

    public UserUpdatedEvent(Long userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
