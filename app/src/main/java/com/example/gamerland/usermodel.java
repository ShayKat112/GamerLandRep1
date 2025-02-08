package com.example.gamerland;

import java.util.List;

public class usermodel {
    private String email;
    private String username;
    private String birthDate;
    private List<String> likedGames;
    private String profileImage;

    public usermodel() {
        // Empty constructor needed for Firestore
    }

    public usermodel(String email, String username, String birthDate, List<String> likedGames, String profileImage) {
        this.email = email;
        this.username = username;
        this.birthDate = birthDate;
        this.likedGames = likedGames;
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public List<String> getLikedGames() {
        return likedGames;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
