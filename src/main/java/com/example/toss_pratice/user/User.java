package com.example.toss_pratice.user;

import java.util.UUID;

public class User {
    private final UUID id;
    private final NickName userName;
    private final Email userEmail;

    public User(UUID id, NickName userName, Email userEmail) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
    }
}
