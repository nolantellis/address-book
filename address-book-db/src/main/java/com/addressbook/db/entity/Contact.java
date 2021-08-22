package com.addressbook.db.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contact")
@Data
@NoArgsConstructor
public class Contact {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private PersonalInfo personalInfo;

  @ElementCollection
  @CollectionTable(name = "phone_details", joinColumns = @JoinColumn(name = "contact_id"))
  private Set<PhoneDetail> phoneDetails = new HashSet<PhoneDetail>();


  @ManyToMany
  @JoinTable(name = "address_book_contact", joinColumns = {@JoinColumn(name = "contact_id")},
      inverseJoinColumns = {@JoinColumn(name = "address_book_id")})
  List<AddressBook> addressBooks = new ArrayList<AddressBook>();



  public static Contact of(String firstName, String lastName, Set<PhoneDetail> phoneDetails) {
    PersonalInfo personalInfo = new PersonalInfo(firstName, lastName);
    return new Contact(personalInfo, phoneDetails);

  }

  public Contact(PersonalInfo personalInfo, Set<PhoneDetail> phoneDetails) {
    super();
    this.personalInfo = personalInfo;
    this.phoneDetails = phoneDetails;
  }

  public void addAddressBook(AddressBook addressBook) {
    addressBooks.add(addressBook);
  }

  public String getFullName() {
    return personalInfo.getFirstName() + " " + personalInfo.getLastName();
  }

}
