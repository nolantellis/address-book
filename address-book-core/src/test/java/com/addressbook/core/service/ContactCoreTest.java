package com.addressbook.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.addressbook.core.builder.ContactBuilder;
import com.addressbook.core.config.CoreConfig;
import com.addressbook.core.exception.AddressBookNotExistException;
import com.addressbook.core.model.ContactModel;
import com.addressbook.core.model.PhoneDetailModel;
import com.addressbook.db.entity.AddressBook;
import com.addressbook.db.entity.Contact;
import com.addressbook.db.entity.PhoneNumberType;
import com.addressbook.db.repository.AddressBookRepository;
import com.addressbook.db.repository.ContactRepository;
import com.github.dozermapper.core.Mapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import lombok.Getter;

@ExtendWith(MockitoExtension.class)
public class ContactCoreTest {

  private static final String TEST_FIRST_TEST_LAST = "TestFirst TestLast";

  private static final String SAMPLE_PHONE = "+41.1234";

  private static final String TEST_LAST = "TestLast";

  private static final String TEST_FIRST = "TestFirst";

  private static final String TEST_BOOK = "TestBook";

  @Mock
  @Getter
  private AddressBookRepository addressBookRepository;

  @Mock
  @Getter
  private ContactRepository contactRepository;

  @Getter
  private ContactService contactService;

  @Getter
  private Mapper mapper;

  @Getter
  private ContactBuilder contactBuilder;

  @Captor
  @Getter
  private ArgumentCaptor<Contact> contactCap;


  @BeforeEach
  public void before() throws Exception {
    mapper = new CoreConfig().beanMapper();
    contactBuilder = new ContactBuilder();
    contactService = new ContactServiceImpl(contactRepository, addressBookRepository, mapper);

  }

  @Test
  public void testCreateContact() {
    // Given

    AddressBook addressBook = new AddressBook(TEST_BOOK);
    Contact contact =
        contactBuilder.withAddressBook(addressBook).withPersonalDetail(TEST_FIRST, TEST_LAST)
            .withPhoneDetail(PhoneNumberType.LANDLINE, SAMPLE_PHONE).buildContact();

    when(addressBookRepository.findById(1L)).thenReturn(Optional.of(addressBook));
    when(contactRepository.saveAndFlush(contactCap.capture())).thenReturn(contact);


    // When
    ContactModel expecteContactModel =
        contactService.createContact(mapper.map(contact, ContactModel.class), 1L);

    // Then
    verify(addressBookRepository, times(1)).findById(1L);
    verify(contactRepository, times(1)).saveAndFlush(contactCap.getValue());

    assertThat(contact.getFullName()).isEqualTo(contactCap.getValue().getFullName());
    assertThat(expecteContactModel.getContactName()).isEqualTo(contact.getFullName());

  }

  @Test
  public void testCreateContactNonExistingAddress() {
    // Given
    AddressBook addressBook = new AddressBook(TEST_BOOK);
    Contact contact =
        contactBuilder.withAddressBook(addressBook).withPersonalDetail(TEST_FIRST, TEST_LAST)
            .withPhoneDetail(PhoneNumberType.LANDLINE, SAMPLE_PHONE).buildContact();

    when(addressBookRepository.findById(1L)).thenReturn(Optional.empty());

    // When
    Assertions.assertThatExceptionOfType(AddressBookNotExistException.class).isThrownBy(() -> {
      contactService.createContact(mapper.map(contact, ContactModel.class), 1L);
    });
    // Then
    verify(addressBookRepository, times(1)).findById(1L);

  }

  @Test
  public void testDeleteContact() {
    // Given
    AddressBook addressBook = new AddressBook(TEST_BOOK);

    when(addressBookRepository.findById(1L)).thenReturn(Optional.of(addressBook));
    when(contactRepository.deleteByIdAndAddressBooksId(1L, 1L)).thenReturn(1L);

    // When
    boolean isDeleted = contactService.deleteByAddressBookIdAndContactById(1L, 1L);

    // Then
    verify(addressBookRepository, times(1)).findById(1L);
    verify(contactRepository, times(1)).deleteByIdAndAddressBooksId(1L, 1L);

    assertThat(isDeleted).isTrue();

  }

  @Test
  public void testGetAllContacts() {
    // Given
    AddressBook addressBook = new AddressBook(TEST_BOOK);
    PageRequest pageInfo = PageRequest.of(0, 20);
    List<Contact> contacts =
        contactBuilder.withAddressBook(addressBook).withPersonalDetail(TEST_FIRST, TEST_LAST)
            .withPhoneDetail(PhoneNumberType.LANDLINE, SAMPLE_PHONE).withRandomName(20)
            .buildContactList();
    Page<Contact> contactsPage = new PageImpl<Contact>(contacts);

    when(addressBookRepository.findById(1L)).thenReturn(Optional.of(addressBook));
    when(contactRepository.findAllByAddressBooksId(1L, pageInfo)).thenReturn(contactsPage);

    // When
    Page<ContactModel> expectedContatPage = contactService.getAllContact(1L, pageInfo);

    // Then
    verify(addressBookRepository, times(1)).findById(1L);
    verify(contactRepository, times(1)).findAllByAddressBooksId(1L, pageInfo);

    assertThat(expectedContatPage.getTotalElements()).isEqualTo(contactsPage.getTotalElements());
    assertThat(expectedContatPage.getTotalPages()).isEqualTo(contactsPage.getTotalPages());

  }

  @Test
  public void testGetUniqueContactDuplicatePhone() {
    // Given
    AddressBook addressBook1 = new AddressBook(TEST_BOOK);
    AddressBook addressBook2 = new AddressBook("Test Addressbok 2");

    Contact contact1 =
        contactBuilder.withAddressBook(addressBook1).withPersonalDetail(TEST_FIRST, TEST_LAST)
            .withPhoneDetail(PhoneNumberType.LANDLINE, SAMPLE_PHONE).buildContact();

    contactBuilder = new ContactBuilder();

    Contact contact2 =
        contactBuilder.withAddressBook(addressBook2).withPersonalDetail(TEST_FIRST, TEST_LAST)
            .withPhoneDetail(PhoneNumberType.LANDLINE, SAMPLE_PHONE).buildContact();

    when(contactRepository.getAll()).thenReturn(Stream.of(contact1, contact2));

    // When
    Map<String, Set<PhoneDetailModel>> contactMap = contactService.getAllUniqueContacts();
    // Then
    verify(contactRepository, times(1)).getAll();

    assertThat(contactMap).isNotEmpty().hasSize(1);
    assertThat(contactMap).containsOnlyKeys(TEST_FIRST_TEST_LAST);
    assertThat(contactMap.get(TEST_FIRST_TEST_LAST)).hasSize(1);

  }

  @Test
  public void testGetUniqueContact2Phones() {
    // Given
    AddressBook addressBook1 = new AddressBook(TEST_BOOK);
    AddressBook addressBook2 = new AddressBook("Test Addressbok 2");

    Contact contact1 =
        contactBuilder.withAddressBook(addressBook1).withPersonalDetail(TEST_FIRST, TEST_LAST)
            .withPhoneDetail(PhoneNumberType.LANDLINE, SAMPLE_PHONE).buildContact();

    contactBuilder = new ContactBuilder();

    Contact contact2 =
        contactBuilder.withAddressBook(addressBook2).withPersonalDetail(TEST_FIRST, TEST_LAST)
            .withPhoneDetail(PhoneNumberType.LANDLINE, "+41.1233").buildContact();

    when(contactRepository.getAll()).thenReturn(Stream.of(contact1, contact2));

    // When
    Map<String, Set<PhoneDetailModel>> contactMap = contactService.getAllUniqueContacts();
    // Then
    verify(contactRepository, times(1)).getAll();

    assertThat(contactMap).isNotEmpty().hasSize(1);
    assertThat(contactMap).containsOnlyKeys(TEST_FIRST_TEST_LAST);
    assertThat(contactMap.get(TEST_FIRST_TEST_LAST)).hasSize(2);
    assertThat(contactMap.get(TEST_FIRST_TEST_LAST)).extracting("phoneNumber")
        .contains(SAMPLE_PHONE, "+41.1233");

  }


}
