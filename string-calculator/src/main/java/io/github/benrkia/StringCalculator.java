package io.github.benrkia;


import java.util.regex.Pattern;
import java.util.stream.Stream;

public class StringCalculator {

  public static final String COMMON_DELIMITERS = "[,\n]";
  public static final String CUSTOM_SINGLE_CHAR_DELIMITER = "//(.)(.+)";
  public static final String CUSTOM_MULTI_CHAR_DELIMITER = "//\\[(.+)](.+)";

  private static boolean largeNumbersPredicate(int n) {
    return n < 1000;
  }

  public int add(final String input) {
    if (input == null || input.isBlank()) return 0;

    return split(input)
        .mapToInt(this::parse)
        .filter(StringCalculator::largeNumbersPredicate)
        .sum();
  }

  private int parse(final String input) {
    final int parsedNumber = Integer.parseInt(input);
    if (parsedNumber < 0) throw new NumberFormatException("Negative numbers are not supported");

    return parsedNumber;
  }

  private Stream<String> split(final String input) {
    if (usesMultiCharsCustomDelimiter(input)) {
      return splitUsingMultiCharsDelimiter(input);
    }
    if (usesSingleCharCustomDelimiter(input)) {
      return splitUsingSingleCharDelimiter(input);
    }

    return Pattern.compile(COMMON_DELIMITERS).splitAsStream(input);
  }

  private Stream<String> splitUsingMultiCharsDelimiter(final String input) {
    final var matcher = Pattern.compile(CUSTOM_MULTI_CHAR_DELIMITER)
        .matcher(input);
    if (matcher.matches()) {
      return split(matcher.group(1), matcher.group(2));
    }
    throw new IllegalStateException("Invalid multi-char delimited expression");
  }

  private Stream<String> splitUsingSingleCharDelimiter(final String input) {
    final var matcher = Pattern.compile(CUSTOM_SINGLE_CHAR_DELIMITER)
        .matcher(input);
    if (matcher.matches()) {
      return split(matcher.group(1), matcher.group(2));
    }
    throw new IllegalStateException("Invalid single-char delimited expression");
  }

  private Stream<String> split(final String delimiter, final String input) {
    return Stream.of(input.split(delimiter));
  }

  private boolean usesSingleCharCustomDelimiter(final String input) {
    return input.startsWith("//");
  }

  private boolean usesMultiCharsCustomDelimiter(final String input) {
    return input.startsWith("//[");
  }

}
