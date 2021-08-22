package com.addressbook.rest.controller;

import com.addressbook.core.exception.AddressBookExistException;
import com.addressbook.core.model.ContactModel;
import com.addressbook.core.model.PersonalInfoModel;
import com.addressbook.core.model.PhoneDetailModel;
import com.addressbook.core.model.PhoneNumberType;
import com.addressbook.core.service.ContactService;
import com.addressbook.rest.model.ContactCreateRequest;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Getter;


@RestController
@RequestMapping("/api/v1/address-book/{addressBookId}/contacts")
public class AddressbookWithContactController {

  private static final String ADDRESS_BOOK = "AddressBook";
  private static final String SORT_ORDER = "personalInfo.firstName,personalInfo.lastName";
  @Autowired
  @Getter
  private ContactService contactService;


  @Operation(summary = "Add Contact to AddressBook", description = "Add Contact to AddressBook",
      tags = {ADDRESS_BOOK},
      responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200",
              content = @Content(schema = @Schema(implementation = ContactModel.class))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure",
              content = @Content(schema = @Schema(hidden = true)))},
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(schema = @Schema(implementation = ContactCreateRequest.class),
              examples = @ExampleObject(
                  value = "{\"firstName\":\"firstName\",\"lastName\":\"lastName\","
                      + "\"phoneNumber\":\"+41.141492658\"}"))))
  @PostMapping("/")
  public ContactModel addContactToAddressBook(
      @Parameter(description = "Id of addressbook", example = "1",
          required = true) @NotNull @PathVariable(name = "addressBookId") Long addressId,
      @Parameter(description = "Contact to add. Cannot null or empty.", required = true,
          schema = @Schema(
              implementation = ContactCreateRequest.class)) @Valid @RequestBody ContactCreateRequest contactCreateRequest) {

    ContactModel contactModel = new ContactModel();
    contactModel.setPersonalInfo(new PersonalInfoModel(contactCreateRequest.getFirstName(),
        contactCreateRequest.getLastName()));
    PhoneDetailModel phone =
        new PhoneDetailModel(PhoneNumberType.LANDLINE, contactCreateRequest.getPhoneNumber());
    contactModel.setPhoneDetails(Set.of(phone));


    return contactService.createContact(contactModel, addressId);
  }


  @Operation(summary = "Get all contacts", description = "Get all contacts", tags = {ADDRESS_BOOK},
      responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200",
              content = @Content(schema = @Schema(type = "object"))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure",
              content = @Content(schema = @Schema(hidden = true)))},
      parameters = {
          @Parameter(in = ParameterIn.QUERY, description = "Page you want to retrieve (0..N)",
              name = "page",
              content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))),
          @Parameter(in = ParameterIn.QUERY, description = "Number of records per page.",
              name = "size",
              content = @Content(schema = @Schema(type = "integer", defaultValue = "20"))),
          @Parameter(in = ParameterIn.QUERY,
              description = "sort by property name Example firstName,lastName", name = "sort",
              content = @Content(schema = @Schema(type = "string", defaultValue = SORT_ORDER)))})
  @GetMapping("/")
  public Page<ContactModel> getAllContacts(
      @Parameter(description = "Id of addressbook", example = "1",
          required = true) @NotNull @PathVariable(name = "addressBookId") Long addressBookId,
      @RequestParam(name = "page") int page, @RequestParam(name = "size") int size,
      @RequestParam(name = "sort") String sort) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
    return contactService.getAllContact(addressBookId, pageable);
  }


  @Operation(summary = "Delete Contact", description = "Delete Contact", tags = {ADDRESS_BOOK},
      responses = {
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure",
              content = @Content(schema = @Schema(hidden = true)))})
  @DeleteMapping("/{id}")
  public boolean deleteContact(
      @Parameter(description = "Id of contact",
          required = true) @NotNull @PathVariable(name = "id") Long id,
      @Parameter(description = "Id of addressbook", required = true,
          example = "1") @NotNull @PathVariable(name = "addressBookId") Long addressBookId)
      throws AddressBookExistException {
    return contactService.deleteByAddressBookIdAndContactById(addressBookId, id);
  }

}
