package com.addressbook.rest;

import static org.assertj.core.api.Assertions.assertThat;
import com.addressbook.core.model.ContactModel;
import com.addressbook.core.model.PhoneNumberType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AddressBookContactIntTest extends AbstractTest {

  @Test
  @Order(1)
  public void testAddContact() throws JSONException {
    // Given
    JSONObject contactJson = new JSONObject();
    contactJson.put("firstName", "firstName");
    contactJson.put("lastName", "lastName");
    contactJson.put("phoneNumber", "+41.1234");

    HttpEntity<String> request = new HttpEntity<String>(contactJson.toString(), headers);

    // When
    ResponseEntity<ContactModel> expectedEntity = this.restTemplate.postForEntity(
        getUrl("/api/v1/address-book/{addressBookId}/contacts/"), request, ContactModel.class, 1);

    // Then
    assertThat(expectedEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(expectedEntity.getBody().getId()).isNotNull();
    assertThat(expectedEntity.getBody().getContactName()).isEqualTo("firstName lastName");
    assertThat(expectedEntity.getBody().getPhoneDetails()).extracting("phoneNumber")
        .contains("+41.1234");
    assertThat(expectedEntity.getBody().getPhoneDetails()).extracting("phoneNumberType")
        .contains(PhoneNumberType.LANDLINE);
  }
  
  @Test
  @Order(2)
  public void testDeleteContact() throws JSONException {
    // Given
    JSONObject contactJson = new JSONObject();
    contactJson.put("firstName", "firstName1");
    contactJson.put("lastName", "lastName1");
    contactJson.put("phoneNumber", "+41.1234");
    HttpEntity<String> request = new HttpEntity<String>(contactJson.toString(), headers);
    ResponseEntity<ContactModel> expectedEntity = this.restTemplate.postForEntity(
        getUrl("/api/v1/address-book/{addressBookId}/contacts/"), request, ContactModel.class, 1);
    assertThat(expectedEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(expectedEntity.getBody().getId()).isNotNull();
    
 // When
    ResponseEntity<Void> expectedEntity1 =
        this.restTemplate.exchange(getUrl("/api/v1/address-book/{addressBookId}/contacts/{id}"), HttpMethod.DELETE, null,
            Void.class, 1,expectedEntity.getBody().getId());

    // Then
    assertThat(expectedEntity1.getStatusCode()).isEqualTo(HttpStatus.OK);    
  }

}
