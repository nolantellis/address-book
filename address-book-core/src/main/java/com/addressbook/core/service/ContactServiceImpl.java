package com.addressbook.core.service;

import com.addressbook.core.exception.AddressBookNotExistException;
import com.addressbook.core.model.ContactModel;
import com.addressbook.core.model.PhoneDetailModel;
import com.addressbook.db.entity.AddressBook;
import com.addressbook.db.entity.Contact;
import com.addressbook.db.repository.AddressBookRepository;
import com.addressbook.db.repository.ContactRepository;
import com.github.dozermapper.core.Mapper;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("contactService")
@AllArgsConstructor
@Slf4j
@Transactional
public class ContactServiceImpl implements ContactService {


  @Autowired
  private ContactRepository contactRepository;
  @Autowired
  private AddressBookRepository addressBookRepository;
  @Autowired
  private Mapper mapper;

  @Override
  public ContactModel createContact(ContactModel contactModel, Long addressBookId) {
    Contact contact = mapper.map(contactModel, Contact.class);
    AddressBook addressBook = validateAndGetAddressBook(addressBookId);
    contact.addAddressBook(addressBook);
    return mapper.map(contactRepository.saveAndFlush(contact), ContactModel.class);
  }



  @Override
  public Page<ContactModel> getAllContact(Long addressBookId, Pageable page) {
    validateAndGetAddressBook(addressBookId);
    return contactRepository.findAllByAddressBooksId(addressBookId, page).map(c -> {
      return mapper.map(c, ContactModel.class);
    });
  }

  @Override
  public boolean deleteByAddressBookIdAndContactById(Long addressBookId, Long contactId) {
    validateAndGetAddressBook(addressBookId);
    long deleted = contactRepository.deleteByIdAndAddressBooksId(contactId, addressBookId);
    return deleted == 1L;

  }

  /**
   * This method is used to get a map of contacts unique by Name. PhoneDetails has hashcode and
   * equals hence. If contacts in 2 sets are different then they get merged. Else same contacts have
   * only 1 value in the set.
   */
  @Override
  public Map<String, Set<PhoneDetailModel>> getAllUniqueContacts() {
    Stream<Contact> contactStream = contactRepository.getAll();
    return contactStream.map(c -> {
      return mapper.map(c, ContactModel.class);
    }).collect(Collectors.toMap(ContactModel::getContactName, ContactModel::getPhoneDetails,
        (set1, set2) -> {
          set1.addAll(set2);
          return set1;
        }));
  }

  private AddressBook validateAndGetAddressBook(Long addressBookId) {
    Optional<AddressBook> addressBook = addressBookRepository.findById(addressBookId);
    log.info("Address book present for id : {} : ispresent {}", addressBookId,
        addressBook.isPresent());
    return addressBook
        .orElseThrow(() -> new AddressBookNotExistException("No Addressbook present"));
  }
}
