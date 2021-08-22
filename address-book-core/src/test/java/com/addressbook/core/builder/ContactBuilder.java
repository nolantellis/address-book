package com.addressbook.core.builder;


import com.addressbook.db.entity.AddressBook;
import com.addressbook.db.entity.Contact;
import com.addressbook.db.entity.PhoneDetail;
import com.addressbook.db.entity.PhoneNumberType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.Getter;

@Getter
public class ContactBuilder {


  private Set<PhoneDetail> phoneDetails = new HashSet<PhoneDetail>();
  private List<Contact> contacts = new ArrayList();
  private AddressBook addressBook;
  private String firstName = "dummyFirst";
  private String lastName = "dummyLast";

  public ContactBuilder withPhoneDetail(PhoneNumberType phoneNumberType, String phoneNumber) {

    phoneDetails.add(new PhoneDetail(phoneNumberType, phoneNumber));
    return this;

  }

  public ContactBuilder withPersonalDetail(String firstName, String lastName) {

    this.firstName = firstName;
    this.lastName = lastName;
    return this;

  }

  public ContactBuilder withAddressBook(AddressBook addressBook) {
    this.addressBook = addressBook;
    return this;
  }

  public ContactBuilder withRandomName(int count) {
    IntStream.range(0, count).forEach(e -> {
      firstName = "firstName" + " " + e;
      contacts.add(buildContact());
    });
    return this;
  }

  public Contact buildContact() {

    var contact = Contact.of(firstName, lastName, phoneDetails);
    contact.addAddressBook(addressBook);
    return contact;
  }

  public List<Contact> buildContactList() {
    return contacts;
  }



}
