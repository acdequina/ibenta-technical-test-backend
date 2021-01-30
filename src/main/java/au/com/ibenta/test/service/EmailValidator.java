package au.com.ibenta.test.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EmailValidator {

    public static void validate(String email) {

        final String emailPattern = "^" + "([a-zA-Z0-9_\\.\\-+])+"
                + "@" + "[a-zA-Z0-9-.]+"
                + "\\." + "[a-zA-Z0-9-]{2,}"
                + "$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidInputException("Invalid email address. | email:" +email);
        }
    }
}
