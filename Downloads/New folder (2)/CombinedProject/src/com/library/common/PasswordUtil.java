package com.library.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification using SHA-256 with salt
 */
public class PasswordUtil {

    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;

    /**
     * Hashes a password with a randomly generated salt
     * 
     * @param password The plain text password
     * @return The hashed password in format: salt:hash
     */
    public static String hashPassword(String password) {
        try {
            // Generate random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Hash password with salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            // Encode salt and hash to Base64
            String saltStr = Base64.getEncoder().encodeToString(salt);
            String hashStr = Base64.getEncoder().encodeToString(hashedPassword);

            // Return in format: salt:hash
            return saltStr + ":" + hashStr;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verifies a password against a stored hash
     * 
     * @param password   The plain text password to verify
     * @param storedHash The stored hash in format: salt:hash
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Split stored hash into salt and hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }

            String saltStr = parts[0];
            String expectedHash = parts[1];

            // Decode salt from Base64
            byte[] salt = Base64.getDecoder().decode(saltStr);

            // Hash the provided password with the same salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            // Encode the hash to Base64
            String actualHash = Base64.getEncoder().encodeToString(hashedPassword);

            // Compare hashes
            return actualHash.equals(expectedHash);
        } catch (Exception e) {
            return false;
        }
    }
}
