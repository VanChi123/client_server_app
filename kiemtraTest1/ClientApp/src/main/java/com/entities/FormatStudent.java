package com.entities;

import java.util.regex.Pattern;

public class FormatStudent {
        private static Pattern DATE_PATTERN = Pattern.compile(
                "^\\d{4}-\\d{2}-\\d{2}$");

        public boolean matches(String date) {
                return DATE_PATTERN.matcher(date).matches();
        }

}
