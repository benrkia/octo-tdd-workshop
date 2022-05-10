package io.github.benrkia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringCalculatorTest {
  // AAA => Arrange, Act, Assert
  // SUT => system under test

  @Test
  void should_return_zero_when_provided_empty_string() {
    // Arrange
    var sut = new StringCalculator();

    // Act
    var actual = sut.sum("");

    // Assert
    assertEquals(0, actual);
  }

  @ParameterizedTest
  @MethodSource
  void should_return_a_number_value(final String input, final int expected) {
    // Arrange
    var sut = new StringCalculator();

    // Act
    var actual = sut.sum(input);

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  void should_sum_comma_delimited_numbers() {
    // Arrange
    var sut = new StringCalculator();

    // Act
    var actual = sut.sum("22,7");

    // Assert
    assertEquals(29, actual);
  }

  @Test
  void should_throw_when_provided_invalid_number() {
    // Arrange
    var sut = new StringCalculator();

    // Assert
    String message = assertThrows(NumberFormatException.class, () -> {
      // Act
      sut.sum("22,ab");
    }).getMessage();

    // Assert
    assertEquals("Invalid number", message);
  }

  @Test
  void should_sum_newline_delimited_numbers() {
    // Arrange
    var sut = new StringCalculator();

    // Act
    var actual = sut.sum("22\n7");

    // Assert
    assertEquals(29, actual);
  }

  @Test
  void should_throw_when_provided_negative_number() {
    // Arrange
    var sut = new StringCalculator();

    // Assert
    String message = assertThrows(NumberFormatException.class, () -> {
      // Act
      sut.sum("23,25\n-47\n-199");
    }).getMessage();

    assertEquals("Negative numbers are not allowed [-47, -199]", message);
  }

  @Test
  void should_ignore_numbers_greater_than_thousand() {
    var sut = new StringCalculator();

    var actual = sut.sum("50,1000\n500,38983");

    assertEquals(550, actual);
  }

  private static Stream<Arguments> should_return_a_number_value() {
    return Stream.of(
        Arguments.of("5", 5),
        Arguments.of("28", 28),
        Arguments.of("500", 500)
    );
  }
}
