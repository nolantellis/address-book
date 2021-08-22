package com.addressbook.core.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDetailModel {
  PhoneNumberType phoneNumberType;
  String phoneNumber;

}
