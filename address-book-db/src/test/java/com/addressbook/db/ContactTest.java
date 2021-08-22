package com.addressbook.db;

import static org.assertj.core.api.Assertions.assertThat;
import com.addressbook.db.builder.ContactBuilder;
import com.addressbook.db.entity.AddressBook;
import com.addressbook.db.entity.Contact;
import com.addressbook.db.entity.PhoneNumberType;
import com.addressbook.db.repository.AddressBookRepository;
import com.addressbook.db.repository.ContactRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.Getter;

@Getter
@Transactional
public class ContactTest extends AbstractBaseTest {

  private static final String PHONE_NUMBER = "0333231323";

  private static final String LAST_NAME = "LastName";

  private static final String FIRST_NAME = "FirstName";

  private AddressBook addressBook;

  @Autowired
  private AddressBookRepository addressBookRepository;

  @Autowired
  private ContactRepository contactRepository;
  private ContactBuilder contactBuilder;

  @BeforeEach
  public void before() {
    contactBuilder = new ContactBuilder();
    addressBook = AddressBook.of("Test AddressBook");
    addressBook = addressBookRepository.saveAndFlush(addressBook);

    assertThat(addressBook.getId()).isNotNull();
  }

  @Test
  public void testAddContactToAddressBook() {
    // Given
    contactBuilder.withAddressBook(addressBook).withPersonalDetail(FIRST_NAME, LAST_NAME)
        .withPhoneDetail(PhoneNumberType.LANDLINE, PHONE_NUMBER);
    Contact contact = contactBuilder.buildContact();
    // When
    Contact expectedContact = contactRepository.saveAndFlush(contact);

    // Then
    assertThat(expectedContact.getId()).isNotNull();
    String firstName = contact.getPersonalInfo().getFirstName();
    assertThat(expectedContact.getPersonalInfo().getFirstName()).isEqualTo(firstName);

    String lastName = contact.getPersonalInfo().getLastName();
    assertThat(expectedContact.getPersonalInfo().getLastName()).isEqualTo(lastName);

  }

  @Test
  public void testfindAllContact() {
    // Given
    List<Contact> contacts =
        contactBuilder.withAddressBook(addressBook).withPersonalDetail(FIRST_NAME, LAST_NAME)
            .withPhoneDetail(PhoneNumberType.LANDLINE, PHONE_NUMBER).withRandomName(20)
            .buildContactList();
    contactRepository.saveAll(contacts);

    // When
    Page<Contact> expectedContacts = contactRepository.findAll(PageRequest.of(0, 10));

    // Then
    assertThat(expectedContacts.getNumberOfElements()).isEqualTo(10);
    assertThat(expectedContacts.getTotalElements()).isEqualTo(20);
    assertThat(expectedContacts.getTotalPages()).isEqualTo(2);

  }


  @Test
  public void testfindContactById() {
    // Given
    List<Contact> contacts =
        contactBuilder.withAddressBook(addressBook).withPersonalDetail(FIRST_NAME, LAST_NAME)
            .withPhoneDetail(PhoneNumberType.LANDLINE, PHONE_NUMBER).withRandomName(20)
            .buildContactList();
    contactRepository.saveAll(contacts);

    // When
    Optional<Contact> expectedContact = contactRepository.findByIdAndAddressBooksId(
        contacts.get(2).getId(), contacts.get(2).getAddressBooks().get(0).getId());

    // Then
    assertThat(expectedContact.isPresent()).isTrue();
    assertThat(expectedContact.get().getId()).isNotNull();
    String firstName = contacts.get(2).getPersonalInfo().getFirstName();
    assertThat(expectedContact.get().getPersonalInfo().getFirstName()).isEqualTo(firstName);

    String lastName = contacts.get(2).getPersonalInfo().getLastName();
    assertThat(expectedContact.get().getPersonalInfo().getLastName()).isEqualTo(lastName);
    assertThat(expectedContact.get().getFullName()).isEqualTo(firstName + " " + lastName);

  }

  @Test
  public void testDeleteContactById() {
    // Given
    List<Contact> contacts =
        contactBuilder.withAddressBook(addressBook).withPersonalDetail(FIRST_NAME, LAST_NAME)
            .withPhoneDetail(PhoneNumberType.LANDLINE, PHONE_NUMBER).withRandomName(20)
            .buildContactList();
    contacts = contactRepository.saveAll(contacts);

    // When
    long expected = contactRepository.deleteByIdAndAddressBooksId(contacts.get(2).getId(),
        contacts.get(2).getAddressBooks().get(0).getId());

    // Then
    assertThat(expected).isEqualTo(1);


  }

}
