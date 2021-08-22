package com.addressbook.rest.controller;

import com.addressbook.core.model.PhoneDetailModel;
import com.addressbook.core.service.ContactService;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Getter;

@RestController
@RequestMapping("/api/v1/contacts")
public class ContactController {

  @Autowired
  @Getter
  private ContactService contactService;

  @Operation(summary = "Get all contacts unique and merged", description = "Get all merged/Unuque contacts",
      tags = {"Contacts"},
      responses = {
          @ApiResponse(description = "Successful Operation", responseCode = "200",
              content = @Content(schema = @Schema(type = "object"))),
          @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          @ApiResponse(responseCode = "401", description = "Authentication Failure",
              content = @Content(schema = @Schema(hidden = true)))})
  @GetMapping("/")
  public Map<String, Set<PhoneDetailModel>> getUniqueMergedContacts() {

    return getContactService().getAllUniqueContacts();

  }

}
