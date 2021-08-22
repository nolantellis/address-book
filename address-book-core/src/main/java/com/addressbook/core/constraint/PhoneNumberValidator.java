package com.addressbook.core.constraint;


import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.Getter;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
  // EPP Pattern of phone number
  @Getter
  private Pattern phonePattern = Pattern.compile("^\\+[0-9]{1,3}\\.[0-9]{4,14}(?:x.+)?$");

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
    return phonePattern.matcher(phoneNumber).matches();

  }

}
