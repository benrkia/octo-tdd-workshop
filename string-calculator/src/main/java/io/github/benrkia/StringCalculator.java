package io.github.benrkia;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringCalculator {

  public static final int MAX_NUMBER_THRESHOLD = 1000;

  public int sum(final String input) {
    if (input.length() == 0)
      return 0;

    var parsedNumbers = parseNumbersBelowThreshold(input);
    ensureNonNegativeNumbers(parsedNumbers);

    int sum = 0;
    for (int number : parsedNumbers) {
      sum += number;
    }
    return sum;
  }

  private void ensureNonNegativeNumbers(final List<Integer> parsedNumbers) {
    List<Integer> negativeNumbers = parsedNumbers.stream()
        .filter(number -> number < 0)
        .collect(Collectors.toList());

    if (!negativeNumbers.isEmpty()) {
      throw new NumberFormatException("Negative numbers are not allowed " + negativeNumbers);
    }
  }

  private List<Integer> parseNumbersBelowThreshold(final String input) {
    return Stream.of(input.split("[,\n]"))
        .map(this::parse)
        .filter(number -> number < MAX_NUMBER_THRESHOLD)
        .collect(Collectors.toList());
  }

  private int parse(final String number) {
    try {
      return Integer.parseInt(number);
    } catch (NumberFormatException e) {
      throw new NumberFormatException("Invalid number");
    }
  }
}
