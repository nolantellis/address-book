package com.addressbook.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.addressbook.core.config.CoreConfig;
import com.addressbook.core.exception.AddressBookExistException;
import com.addressbook.core.exception.ContactExistsForAddressBookException;
import com.addressbook.core.model.AddressBookModel;
import com.addressbook.db.entity.AddressBook;
import com.addressbook.db.repository.AddressBookRepository;
import com.addressbook.db.repository.ContactRepository;
import com.github.dozermapper.core.Mapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import lombok.Getter;

@ExtendWith(MockitoExtension.class)
public class AddressBookCoreTest {


  private static final String TEST_ADDRESS_BOOK = "Test AddressBook";

  @Mock
  @Getter
  private AddressBookRepository addressBookRepository;

  @Mock
  @Getter
  private ContactRepository contactRepository;

  @Getter
  private AddressBookService addressBookService;

  @Getter
  private Mapper mapper;

  @Captor
  @Getter
  private ArgumentCaptor<AddressBook> addressBookCaptor;


  @BeforeEach
  public void before() throws Exception {
    mapper = new CoreConfig().beanMapper();
    addressBookService =
        new AddressBookServiceImpl(addressBookRepository, contactRepository, mapper);

  }

  @Test
  public void testgetAddressBook() {

    // Given
    AddressBook dbAddressBook = AddressBook.of(TEST_ADDRESS_BOOK);
    when(addressBookRepository.findAll()).thenReturn(List.of(dbAddressBook));

    // When
    List<AddressBookModel> addAddressBookModel = addressBookService.getAllAddressBook();

    // Then
    verify(addressBookRepository, times(1)).findAll();
    assertThat(addAddressBookModel).isNotNull();
    assertThat(addAddressBookModel).hasSize(1);
    assertThat(addAddressBookModel).extracting("name").containsExactly(dbAddressBook.getName());
  }


  @Test
  public void testgetEmptyAddressBook() {

    // Given
    when(addressBookRepository.findAll()).thenReturn(new ArrayList<AddressBook>());

    // When
    List<AddressBookModel> addAddressBookModel = addressBookService.getAllAddressBook();

    // Then
    verify(addressBookRepository, times(1)).findAll();
    assertThat(addAddressBookModel).isEmpty();
  }

  @Test
  public void testAddExistingAddressBook() {

    // Given
    AddressBook dbAddressBook = AddressBook.of(TEST_ADDRESS_BOOK);

    when(addressBookRepository.findByName(TEST_ADDRESS_BOOK))
        .thenReturn(Optional.of(dbAddressBook));

    // When
    Assertions.assertThatExceptionOfType(AddressBookExistException.class).isThrownBy(() -> {
      addressBookService.addAddressBook(TEST_ADDRESS_BOOK);
    });

    // Then
    verify(addressBookRepository, times(1)).findByName(TEST_ADDRESS_BOOK);


  }

  @Test
  public void testAddNewAddressBook() {

    // Given
    AddressBook dbAddressBook = AddressBook.of(TEST_ADDRESS_BOOK);
    when(addressBookRepository.saveAndFlush(addressBookCaptor.capture())).thenReturn(dbAddressBook);
    when(addressBookRepository.findByName(TEST_ADDRESS_BOOK)).thenReturn(Optional.empty());

    // When
    AddressBookModel addAddressBookModel = addressBookService.addAddressBook(TEST_ADDRESS_BOOK);

    // Then
    assertThat(addAddressBookModel).isNotNull();
    verify(addressBookRepository, times(1)).saveAndFlush(addressBookCaptor.getValue());
    verify(addressBookRepository, times(1)).findByName(TEST_ADDRESS_BOOK);


  }

  // if (contactRepository.existsByAddressBooksId(id)) {
  // throw new ContactExistsForAddressBookException("Contact exists for addressbook");
  // }
  // addressBookRepository.deleteById(id);
  // return !addressBookRepository.existsById(id);
  //
  @Test
  public void testDeleteAddressBook() {

    // Given
    when(contactRepository.existsByAddressBooksId(1L)).thenReturn(false);
    doNothing().when(addressBookRepository).deleteById(1L);
    when(addressBookRepository.existsById(1L)).thenReturn(false);

    // When
    boolean status = addressBookService.deleteAddressBook(1L);

    // Then
    assertThat(status).isTrue();
    verify(addressBookRepository, times(1)).deleteById(1L);
    verify(addressBookRepository, times(1)).existsById(1L);
    verify(contactRepository, times(1)).existsByAddressBooksId(1L);


  }

  @Test
  public void testDeleteAddressBookWithContact() {

    // Given
    when(contactRepository.existsByAddressBooksId(1L)).thenReturn(true);

    Assertions.assertThatExceptionOfType(ContactExistsForAddressBookException.class)
        .isThrownBy(() -> {
          addressBookService.deleteAddressBook(1L);
        });
    // Then
    verify(contactRepository, times(1)).existsByAddressBooksId(1L);


  }

}
