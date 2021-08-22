package com.addressbook.db.repository;

import com.addressbook.db.entity.AddressBook;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {

  List<AddressBook> findByNameContaining(String name);

  Optional<AddressBook> findByName(String name);



}
