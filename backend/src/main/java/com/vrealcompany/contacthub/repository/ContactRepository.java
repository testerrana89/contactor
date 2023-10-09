package com.vrealcompany.contacthub.repository;

import com.vrealcompany.contacthub.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByEmailAndCompanyName(String email, String companyName);
}
