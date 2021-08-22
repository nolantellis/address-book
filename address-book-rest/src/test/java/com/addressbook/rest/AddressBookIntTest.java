package com.addressbook.rest;

import static org.assertj.core.api.Assertions.assertThat;
import com.addressbook.core.model.AddressBookModel;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AddressBookIntTest extends AbstractTest {


  

  @Test
  @Order(1)
  public void testCreateAddressBook() throws JSONException {
    // Given
    JSONObject addressNameJson = new JSONObject();
    addressNameJson.put("name", "test Name");
    HttpEntity<String> request = new HttpEntity<String>(addressNameJson.toString(), headers);

    // When
    ResponseEntity<AddressBookModel> expectedEntity = this.restTemplate
        .postForEntity(getUrl("/api/v1/address-book/"), request, AddressBookModel.class);

    // Then
    assertThat(expectedEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(expectedEntity.getBody().getId()).isNotNull();
    assertThat(expectedEntity.getBody().getName()).isEqualTo("test Name");

  }

  @Test
  @Order(2)
  public void testGetAllAddressBook() throws JSONException {

    // When
    ResponseEntity<AddressBookModel[]> expectedEntity =
        this.restTemplate.getForEntity(getUrl("/api/v1/address-book/"), AddressBookModel[].class);

    // Then
    assertThat(expectedEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(expectedEntity.getBody().length).isEqualTo(2);
    assertThat(expectedEntity.getBody()).extracting("name").contains("test", "testName");

  }

  @Test
  @Order(3)
  public void testDeleteAddressBook() throws JSONException {

    // Given
    JSONObject addressNameJson = new JSONObject();
    addressNameJson.put("name", "test Name 2");
    HttpEntity<String> request = new HttpEntity<String>(addressNameJson.toString(), headers);
    ResponseEntity<AddressBookModel> expectedEntity = this.restTemplate
        .postForEntity(getUrl("/api/v1/address-book/"), request, AddressBookModel.class);
    assertThat(expectedEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    // When
    ResponseEntity<Void> expectedEntity1 =
        this.restTemplate.exchange(getUrl("/api/v1/address-book/{id}"), HttpMethod.DELETE, null,
            Void.class, expectedEntity.getBody().getId());

    // Then
    assertThat(expectedEntity1.getStatusCode()).isEqualTo(HttpStatus.OK);


  }



}
