package com.vrealcompany.contacthub.controller;

import com.vrealcompany.contacthub.model.Contact;
import com.vrealcompany.contacthub.model.filter.ContactFilter;
import com.vrealcompany.contacthub.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public ResponseEntity<List<Contact>> getContacts() {
        List<Contact> contactList = contactService.getContacts();
        return ResponseEntity.ok(contactList);
    }

    @GetMapping("{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Contact contact = contactService.getContactById(id);
        return ResponseEntity.ok(contact);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Contact>> getContactsByFilter(@ModelAttribute ContactFilter filter) {
        List<Contact> contactList = contactService.getContactsByFilter(filter);
        return ResponseEntity.ok(contactList);
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@Valid @RequestBody Contact contact) {
        Contact savedContact = contactService.createContact(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
    }

    @PutMapping("{id}")
    public ResponseEntity<Contact> createContact(@PathVariable Long id, @RequestBody Contact contact) {
        contact.setId(id);
        Contact updatedContact = contactService.updateContact(contact);
        return ResponseEntity.ok(updatedContact);
    }
}
