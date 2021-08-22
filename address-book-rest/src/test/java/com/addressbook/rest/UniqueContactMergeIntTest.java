package com.addressbook.rest;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UniqueContactMergeIntTest extends AbstractTest {

  private static final String _41_1234545 = "+41.1234545";

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  @Order(1)
  public void testGetUniqueContactList() throws JSONException {

    // Given
    addContact("firstName1", "lastName1", _41_1234545);
    addContact("firstName1", "lastName1", _41_1234545);
    addContact("firstName1", "lastName1", "+41.124454545");
    // When
    ResponseEntity<Map> expectedEntity =
        this.restTemplate.getForEntity(getUrl("/api/v1/contacts/"), Map.class);
    // Then

    assertThat(expectedEntity.getBody()).isNotEmpty().hasSize(1);
    List<Map> list = (List<Map>) expectedEntity.getBody().get("firstName1 lastName1");
    assertThat(list).hasSize(2);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  @Order(2)
  public void testGetUniqueContactList2Names() throws JSONException {

    // Given
    addContact("firstName3", "lastName3", _41_1234545);
    addContact("firstName3", "lastName3", _41_1234545);
    addContact("firstName4", "lastName4", "+41.124454545");
    // When
    ResponseEntity<Map> expectedEntity =
        this.restTemplate.getForEntity(getUrl("/api/v1/contacts/"), Map.class);
    // Then

    assertThat(expectedEntity.getBody()).isNotEmpty().hasSize(3);
    List<Map> list = (List<Map>) expectedEntity.getBody().get("firstName3 lastName3");
    assertThat(list).hasSize(1);

    List<Map> list1 = (List<Map>) expectedEntity.getBody().get("firstName4 lastName4");
    assertThat(list1).hasSize(1);
  }

  private void addContact(String firstName, String lastName, String phone) throws JSONException {

    JSONObject contactJson = new JSONObject();
    contactJson.put("firstName", firstName);
    contactJson.put("lastName", lastName);
    contactJson.put("phoneNumber", phone);
    HttpEntity<String> request = new HttpEntity<String>(contactJson.toString(), headers);
    ResponseEntity<String> expectedEntity = this.restTemplate.postForEntity(
        getUrl("/api/v1/address-book/{addressBookId}/contacts/"), request, String.class, 1);
    assertThat(expectedEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(expectedEntity.getBody()).isNotNull();
  }

}
