package com.epam.gymappHibernate.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameGenerator {
    public static String generateUsername(String firstName, String lastName, List<String> existingUsernames) {
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("First name and last name cannot be null");
        }
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int counter = 1;

        while (existingUsernames.contains(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }

}
