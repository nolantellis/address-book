package com.addressbook.rest.model;

import com.addressbook.core.constraint.PhoneNumber;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactCreateRequest {

  @NotBlank(message = "FirstName is rquired")
  public String firstName = "firstName";
  @NotBlank(message = "LastName is rquired")
  public String lastName = "lastName";

  @PhoneNumber
  public String phoneNumber = "+41.4444444";


}
