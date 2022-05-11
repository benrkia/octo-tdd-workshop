package io.github.benrkia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class StringCalculatorTest {
  private final StringCalculator calculator = new StringCalculator();

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"\n", "\r", "\t", " "})
  void should_return_zero_for_null_and_empty_strings(final String input) {
    var actual = calculator.add(input);

    assertEquals(0, actual);
  }

  @ParameterizedTest
  @ValueSource(strings = {"str", "1.5", "38,\n47", "863\n,26"})
  void should_throw_when_provided_invalid_input(final String input) {
    assertThrows(NumberFormatException.class, () -> calculator.add(input));
  }

  @ParameterizedTest
  @MethodSource
  void should_return_number_when_provided_a_single_number(final String input, final int expected) {
    var actual = calculator.add(input);

    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @MethodSource
  void should_sum_numbers_delimited_by_either_newline_or_comma(final String input, final int expected) {
    var actual = calculator.add(input);

    assertEquals(expected, actual);
  }

  @Test
  void should_throw_when_provided_negative_numbers() {
    final var message = assertThrows(NumberFormatException.class,
        () -> calculator.add("-1")).getMessage();
    assertEquals("Negative numbers are not supported", message);
  }

  @Test
  void should_return_sum_ignoring_numbers_greater_than_1000() {
    var actual = calculator.add("37,853\n1000,963\n783979");
    assertEquals(1853, actual);
  }

  @Test
  void should_sum_numbers_delimited_by_a_custom_single_char_delimiter() {
    var actual = calculator.add("//#28#1000#983#894#");

    assertEquals(1905, actual);
  }

  @Test
  void should_sum_numbers_delimited_by_a_custom_multi_char_delimiter() {
    var actual = calculator.add("//[#@%]299#@%17#@%89389#@%26#@%825");

    assertEquals(1167, actual);
  }

  @Test
  void should_throw_when_provided_an_invalid_custom_multi_char_delimiter() {
    final var message = assertThrows(IllegalStateException.class, () -> calculator.add("//[@@304"))
        .getMessage();

    assertEquals("Invalid multi-char delimited expression", message);
  }

  @Test
  void should_throw_when_provided_an_invalid_custom_single_char_delimiter() {
    final var message = assertThrows(IllegalStateException.class, () -> calculator.add("//@"))
        .getMessage();

    assertEquals("Invalid single-char delimited expression", message);
  }

  private static Stream<Arguments> should_return_number_when_provided_a_single_number() {
    return Stream.of(
        Arguments.of("0", 0),
        Arguments.of("3", 3),
        Arguments.of("60", 60)
    );
  }

  private static Stream<Arguments> should_sum_numbers_delimited_by_either_newline_or_comma() {
    return Stream.of(
        Arguments.of("1,3,17", 21),
        Arguments.of("5\n20\n17", 42),
        Arguments.of("13,20\n46\n90", 169)
    );
  }

}
