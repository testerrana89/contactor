package com.vrealcompany.contacthub.service;

import com.vrealcompany.contacthub.controller.ApiHandleException;
import com.vrealcompany.contacthub.model.Contact;
import com.vrealcompany.contacthub.model.filter.ContactFilter;
import com.vrealcompany.contacthub.repository.ContactRepository;
import com.vrealcompany.contacthub.service.client.ContactClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ContactServiceImpl implements ContactService {

    Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);
    @Autowired
    ContactRepository contactRepository;

    @Autowired
    private ContactClient contactClient;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Contact> getContacts() {
        logger.info("Retrieving all contacts");
        List<Contact> contactsFromDb = contactRepository.findAll();
        List<Contact> contactsFromApi = contactClient.getAllContacts();
        return Stream.concat(contactsFromDb.stream(), contactsFromApi.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Contact getContactById(Long id) {
        logger.info("Retrieving contact by id: {}", id);
        Contact contact = null;
        contact = contactClient.getContactById(id);
        if(contact == null){
            Optional<Contact> optionalContact = contactRepository.findById(id);
            if(optionalContact.isEmpty()){
                throw new EntityNotFoundException("Contact not found with id: " + id);
            }
            contact = optionalContact.get();
        }

        return contact;
    }

    @Override
    public List<Contact> getContactsByFilter(ContactFilter filter) {
        logger.info("Filtering contacts with name: {}", filter.getName());
        List<Contact> contactsFromApi = contactClient.getAllContacts().stream()
                .filter(contact -> filter.getName() == null || contact.getName().contains(filter.getName())).toList();
        List<Contact> contactsFromDb =  getTypedQueryByFilter(filter).getResultList();

        return Stream.concat(contactsFromDb.stream(), contactsFromApi.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Contact createContact(Contact contact) {
        if(isExistingContact(contact)) {
            logger.info("Entity already present with email: {} and company-name: {}.", contact.getEmail(), contact.getCompanyName());
            throw new IllegalArgumentException("Contact already exists in the system with same email for the company.");
        }
        logger.info("Creating new contact with email: {} and company: {}", contact.getEmail(), contact.getCompanyName());
        try {
            Contact contactSavedInRemote = contactClient.createContact(contact);
            return contactRepository.save(contactSavedInRemote);
        } catch (Exception e) {
            logger.error("Error creating contact with email: {} and company: {}", contact.getEmail(), contact.getCompanyName());
            throw new ApiHandleException("Error Occurred while creating Contact");
        }
    }

    @Override
    public void createContacts(List<Contact> contacts) {
        contacts.forEach(contact -> {
            if(!isExistingContact(contact)) {
                contactRepository.save(contact);
            }
        });
    }

    @Override
    public Contact updateContact(Contact contact) {
        Optional<Contact> existingContact = contactRepository.findById(contact.getId());
        if(existingContact.isEmpty())
            throw new EntityNotFoundException();

        /**
         * Assuming user can only edit few of the properties
         */
        Contact tempContact = existingContact.get();
        tempContact.setName(contact.getName());
        tempContact.setPhone(contact.getPhone());
        tempContact.setWebsite(contact.getWebsite());

        logger.info("Updating new contact with email: {} and company: {}", contact.getEmail(), contact.getCompanyName());
        try {
            Contact contactSavedInRemote = contactClient.updateContact(contact);
            return contactRepository.save(contactSavedInRemote);
        } catch (Exception e) {
            logger.error("Error updating contact with email: {} and company: {}", contact.getEmail(), contact.getCompanyName());
            throw new ApiHandleException("Error occurred while updating Contact" + e);
        }
    }

    TypedQuery<Contact> getTypedQueryByFilter(ContactFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> cq = cb.createQuery(Contact.class);
        Root<Contact> contact = cq.from(Contact.class);
        List<Predicate> predicates = new ArrayList<>();
        if (filter != null && filter.getName() != null && !filter.getName().isEmpty()) {
            predicates.add(cb.like(cb.lower(contact.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq);
    }

    boolean isExistingContact(Contact contact) {
        Optional<Contact> existingContact =
                contactRepository.findByEmailAndCompanyName(contact.getEmail(), contact.getCompanyName());
        return existingContact.isPresent();
    }
}
