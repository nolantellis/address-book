package com.addressbook.core.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ContactModel {

  private Long id;

  private PersonalInfoModel personalInfo;

  private Set<PhoneDetailModel> phoneDetails = new HashSet<PhoneDetailModel>();

  List<AddressBookModel> addressBooks = new ArrayList<AddressBookModel>();

  public String getContactName() {
    return personalInfo.getFirstName() + " " + personalInfo.getLastName();
  }


}
