package com.addressbook.core.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AddressBookNotExistException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public AddressBookNotExistException(String message) {
    super(message);
  }

}
