package com.addressbook.core.service;

import com.addressbook.core.model.ContactModel;
import com.addressbook.core.model.PhoneDetailModel;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {

  Page<ContactModel> getAllContact(Long addressBookId, Pageable page);

  ContactModel createContact(ContactModel contactModel, Long addressId);

  boolean deleteByAddressBookIdAndContactById(Long addressBookId, Long contactId);

  Map<String, Set<PhoneDetailModel>> getAllUniqueContacts();

}
