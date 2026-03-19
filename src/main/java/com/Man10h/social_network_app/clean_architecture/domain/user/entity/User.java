package com.Man10h.social_network_app.clean_architecture.domain.user.entity;

import com.Man10h.social_network_app.clean_architecture.domain.shared.ImageData;

public final class User {
    private final String id;
    private final String username;
    private final String email;
    private final String passwordHash;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private final boolean enabled;
    private final UserRole role;
    private final ImageData profileImage;

    public User(
            String id,
            String username,
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            String gender,
            boolean enabled,
            UserRole role,
            ImageData profileImage
    ) {
        this.id = id;
        this.username = requireValue(username, "Username");
        this.email = requireValue(email, "Email");
        this.passwordHash = passwordHash == null ? "" : passwordHash;
        this.firstName = normalize(firstName);
        this.lastName = normalize(lastName);
        this.gender = normalize(gender);
        this.enabled = enabled;
        this.role = role;
        this.profileImage = profileImage;
    }

    public static User registerNew(
            String username,
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            String gender,
            UserRole role
    ) {
        return new User(
                null,
                username,
                email,
                requireValue(passwordHash, "Password"),
                firstName,
                lastName,
                gender,
                true,
                role,
                null
        );
    }

    public static User registerExternal(
            String username,
            String email,
            String firstName,
            String lastName,
            String gender,
            UserRole role
    ) {
        return new User(null, username, email, "", firstName, lastName, gender, true, role, null);
    }

    public User updateProfile(String firstName, String lastName, String gender) {
        return new User(
                id,
                username,
                email,
                passwordHash,
                firstName == null || firstName.isBlank() ? this.firstName : firstName.trim(),
                lastName == null || lastName.isBlank() ? this.lastName : lastName.trim(),
                gender == null || gender.isBlank() ? this.gender : gender.trim(),
                enabled,
                role,
                profileImage
        );
    }

    public User resetPassword(String passwordHash) {
        return new User(id, username, email, requireValue(passwordHash, "Password"), firstName, lastName, gender, enabled, role, profileImage);
    }

    public User withProfileImage(ImageData profileImage) {
        return new User(id, username, email, passwordHash, firstName, lastName, gender, enabled, role, profileImage);
    }

    public User toggleEnabled() {
        return new User(id, username, email, passwordHash, firstName, lastName, gender, !enabled, role, profileImage);
    }

    public boolean canAuthenticate() {
        return enabled;
    }

    public String displayName() {
        String fullName = (firstName + " " + lastName).trim();
        return fullName.isBlank() ? username : fullName;
    }

    public String id() {
        return id;
    }

    public String username() {
        return username;
    }

    public String email() {
        return email;
    }

    public String passwordHash() {
        return passwordHash;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String gender() {
        return gender;
    }

    public boolean enabled() {
        return enabled;
    }

    public UserRole role() {
        return role;
    }

    public ImageData profileImage() {
        return profileImage;
    }

    private static String requireValue(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value.trim();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
