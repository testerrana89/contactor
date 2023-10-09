package com.vrealcompany.contacthub.service.client;

import com.vrealcompany.contacthub.model.Contact;

import java.util.List;

public interface ContactClient {
    List<Contact> getAllContacts();

    Contact getContactById(Long id);

    Contact createContact(Contact contact);

    Contact updateContact(Contact contact);
}
