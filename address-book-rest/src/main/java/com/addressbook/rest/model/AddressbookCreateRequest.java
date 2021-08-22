package com.addressbook.rest.model;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressbookCreateRequest {

  @NotNull(message = "Name Cannot be null")
  String name;

}
