package com.vrealcompany.contacthub.service;

import com.vrealcompany.contacthub.model.Contact;
import com.vrealcompany.contacthub.model.filter.ContactFilter;

import java.util.List;

public interface ContactService {
    List<Contact> getContacts();

    Contact getContactById(Long id);

    List<Contact> getContactsByFilter(ContactFilter filter);

    Contact createContact(Contact contact);

    void createContacts(List<Contact> contacts);

    Contact updateContact(Contact contact);
}
