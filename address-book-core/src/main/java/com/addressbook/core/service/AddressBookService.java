package com.addressbook.core.service;

import com.addressbook.core.exception.AddressBookExistException;
import com.addressbook.core.exception.ContactExistsForAddressBookException;
import com.addressbook.core.model.AddressBookModel;
import java.util.List;

public interface AddressBookService {

  List<AddressBookModel> getAllAddressBook();

  AddressBookModel addAddressBook(String name) throws AddressBookExistException;

  boolean deleteAddressBook(Long id) throws ContactExistsForAddressBookException;

}
