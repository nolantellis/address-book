package com.addressbook.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address_book")
@NoArgsConstructor
@Getter
@Setter
public class AddressBook {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  public AddressBook(String name) {
    this.name = name;
  }

  public static AddressBook of(String name) {
    return new AddressBook(name);

  }

}
