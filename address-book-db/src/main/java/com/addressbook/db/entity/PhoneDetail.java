package com.addressbook.db.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhoneDetail {

  @Enumerated(EnumType.STRING)
  @Column(name = "phone_number_type")
  private PhoneNumberType phoneNumberType;

  @Column(name = "phone_number")
  private String phoneNumber;

}
