package com.addressbook.db;


import static org.assertj.core.api.Assertions.assertThat;
import com.addressbook.db.entity.AddressBook;
import com.addressbook.db.repository.AddressBookRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AddressBookTest extends AbstractBaseTest {

  private static final String TEST_ADDRESS_BOOK_FOR_ALL = "TestAddressBook for all";
  private static final String TEST_ADDRESS_BOOK_EXISTING = "TestAddressBook Existing";
  private static final String TEST_ADDRESS_BOOK = "TestAddressBook";
  @Autowired
  private transient AddressBookRepository addressBookRepository;

  @Test
  public void testAddAddressBook() {
    // Given
    AddressBook addressBook = new AddressBook(TEST_ADDRESS_BOOK);
    // When
    AddressBook expectedAddressBook = addressBookRepository.saveAndFlush(addressBook);

    // Then
    assertThat(expectedAddressBook.getId()).isNotNull();
    assertThat(expectedAddressBook.getName()).isEqualTo(addressBook.getName());

  }

  @Test
  public void testFindAddressBookById() {
    // Given
    AddressBook addressBook = new AddressBook(TEST_ADDRESS_BOOK_EXISTING);
    addressBook = addressBookRepository.saveAndFlush(addressBook);
    // When
    Optional<AddressBook> expectedAddressBook = addressBookRepository.findById(addressBook.getId());

    // Then
    assertThat(expectedAddressBook.isPresent()).isTrue();
    assertThat(expectedAddressBook.get().getName()).isEqualTo(addressBook.getName());
    assertThat(expectedAddressBook.get().getId()).isEqualTo(addressBook.getId());

  }

  @Test
  public void testFindAllAddressBook() {
    // Given
    AddressBook addressBook = new AddressBook(TEST_ADDRESS_BOOK_FOR_ALL);
    addressBook = addressBookRepository.saveAndFlush(addressBook);
    // When
    List<AddressBook> expectedAddressBook = addressBookRepository.findAll();

    // Then
    assertThat(expectedAddressBook).isNotEmpty();
    assertThat(expectedAddressBook).extracting("name").contains(addressBook.getName());

  }

}
