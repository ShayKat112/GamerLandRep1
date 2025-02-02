package com.example.gamerland;

import java.util.List;

public class User {
    private String email;
    private String username;// Be cautious about storing passwords in plain text.
    private List<String> likedGames;

    public User(String email, String username, List<String> likedGames) {
        this.email = email;
        this.username = username;
        this.likedGames = likedGames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getLikedGames() {
        return likedGames;
    }

    public void setLikedGames(List<String> likedGames) {
        this.likedGames = likedGames;
    }
}
