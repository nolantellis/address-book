package com.addressbook.core.exception;

public class AddressBookExistException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public AddressBookExistException(String message) {
    super(message);
  }

}
