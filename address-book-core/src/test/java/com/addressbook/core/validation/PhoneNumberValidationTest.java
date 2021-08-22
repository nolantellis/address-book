package com.addressbook.core.validation;

import static org.assertj.core.api.Assertions.assertThat;
import com.addressbook.core.constraint.PhoneNumberValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import lombok.Getter;

public class PhoneNumberValidationTest {

  @Getter
  private PhoneNumberValidator validator = new PhoneNumberValidator();


  @ParameterizedTest
  @CsvSource({"+41.2121212, true", "2121212, false", "aaaa, false", "+41.33334454, true"})
  public void testValidNumbers(ArgumentsAccessor arguments) {
    String phoneString = arguments.getString(0);
    boolean isValid = Boolean.valueOf(arguments.getString(1));
    assertThat(validator.isValid(phoneString, null)).isEqualTo(isValid);
  }

}
