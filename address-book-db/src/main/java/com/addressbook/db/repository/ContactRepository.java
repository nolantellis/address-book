package com.addressbook.db.repository;

import com.addressbook.db.entity.Contact;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

  Page<Contact> findAll(Pageable pageable);

  Optional<Contact> findByIdAndAddressBooksId(Long id, Long addressId);

  @Transactional
  long deleteByIdAndAddressBooksId(Long id, Long addressId);

  Page<Contact> findAllByAddressBooksId(Long addressBookId, Pageable page);

  @Transactional
  @Query("Select c from Contact c")
  Stream<Contact> getAll();

  boolean existsByAddressBooksId(Long addressbookId);

}
