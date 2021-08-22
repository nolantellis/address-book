package com.addressbook.core.exception;

public class ContactExistsForAddressBookException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ContactExistsForAddressBookException(String message) {
    super(message);
  }


}
