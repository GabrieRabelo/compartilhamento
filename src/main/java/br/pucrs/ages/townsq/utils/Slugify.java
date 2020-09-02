package br.pucrs.ages.townsq.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class Slugify {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String toSlug(String input) {
        String no_whitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(no_whitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
