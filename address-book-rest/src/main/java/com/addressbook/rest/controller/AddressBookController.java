package com.addressbook.rest.controller;

import com.addressbook.core.exception.AddressBookExistException;
import com.addressbook.core.exception.ContactExistsForAddressBookException;
import com.addressbook.core.model.AddressBookModel;
import com.addressbook.core.service.AddressBookService;
import com.addressbook.rest.model.AddressbookCreateRequest;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Getter;

/**
 * 
 * @author nolantellis
 *
 *         This controller is used for addressbook only. It exposes create and get addressbook, Is
 *         Id can be used for sub sequent commands for contacts
 */
@RestController
@RequestMapping("/api/v1/address-book")
public class AddressBookController {


  @Autowired
  @Getter
  private AddressBookService addressBookService;


  @Operation(summary = "Get all addressbook", description = "Get all addressbook",
      tags = {"AddressBook"},
      responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = AddressBookModel.class)))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure",
              content = @Content(schema = @Schema(hidden = true)))})
  @GetMapping("/")
  public List<AddressBookModel> getAllAddressBook() {
    return addressBookService.getAllAddressBook();

  }


  @Operation(summary = "Add New Addressbook", description = "Add New Addressbook",
      tags = {"AddressBook"},
      responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200",
              content = @Content(schema = @Schema(implementation = AddressBookModel.class))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure",
              content = @Content(schema = @Schema(hidden = true)))})
  @PostMapping("/")
  public AddressBookModel addNewAddressBook(
      @Parameter(description = "Name for Addressbook",
          required = true) @Valid @RequestBody AddressbookCreateRequest addressbookCreateRequest)
      throws AddressBookExistException {
    return addressBookService.addAddressBook(addressbookCreateRequest.getName());

  }

  @Operation(summary = "Delete Addressbook", description = "Delete Addressbook",
      tags = {"AddressBook"},
      responses = {
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure",
              content = @Content(schema = @Schema(hidden = true)))})
  @DeleteMapping("/{id}")
  public boolean deleteAddressBook(
      @Parameter(description = "Id of addressbook", example = "1",
          required = true) @NotNull @PathVariable(name = "id") Long id)
      throws AddressBookExistException, ContactExistsForAddressBookException {
    return addressBookService.deleteAddressBook(id);
  }



}
