package com.addressbook.core.service;

import com.addressbook.core.exception.AddressBookExistException;
import com.addressbook.core.exception.ContactExistsForAddressBookException;
import com.addressbook.core.model.AddressBookModel;
import com.addressbook.db.entity.AddressBook;
import com.addressbook.db.repository.AddressBookRepository;
import com.addressbook.db.repository.ContactRepository;
import com.github.dozermapper.core.Mapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("addressBookService")
@AllArgsConstructor
@Transactional
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {
  private AddressBookRepository addressBookRepository;
  private ContactRepository contactRepository;
  private Mapper mapper;

  @Override
  public List<AddressBookModel> getAllAddressBook() {

    return addressBookRepository.findAll().stream().map(e -> {
      return mapper.map(e, AddressBookModel.class);
    }).collect(Collectors.toList());

  }

  @Override
  public AddressBookModel addAddressBook(String name) throws AddressBookExistException {
    validateAddressBookExists(name);
    AddressBook addressBook = new AddressBook(name);
    addressBook = addressBookRepository.saveAndFlush(addressBook);
    return mapper.map(addressBook, AddressBookModel.class);

  }

  @Override
  public boolean deleteAddressBook(Long id) throws ContactExistsForAddressBookException {
    if (contactRepository.existsByAddressBooksId(id)) {
      throw new ContactExistsForAddressBookException("Contact exists for addressbook");
    }
    addressBookRepository.deleteById(id);
    return !addressBookRepository.existsById(id);

  }


  private void validateAddressBookExists(String name) throws AddressBookExistException {
    Optional<AddressBook> optionalAddressBook = addressBookRepository.findByName(name);

    if (optionalAddressBook.isPresent()) {
      log.warn("Address book exists with name : {}", name);
      throw new AddressBookExistException("Address book exists");
    }

  }


}
