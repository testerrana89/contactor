package com.vrealcompany.contacthub.service;

import com.vrealcompany.contacthub.controller.ApiHandleException;
import com.vrealcompany.contacthub.model.Contact;
import com.vrealcompany.contacthub.model.filter.ContactFilter;
import com.vrealcompany.contacthub.repository.ContactRepository;
import com.vrealcompany.contacthub.service.auth.JwtServiceImpl;
import com.vrealcompany.contacthub.service.client.ContactClientImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ContactServiceImplTest {
    @Mock
    private ContactRepository contactRepository;

    @MockBean
    private JwtServiceImpl jwtServiceImpl;

    @Mock
    private ContactClientImpl contactClientImpl;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Contact> criteriaQuery;

    @Mock
    private Root<Contact> root;

    @Mock
    private TypedQuery<Contact> typedQuery;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    public void testWhenCalledGetContacts_returnAllGetContactsCombined() {
        List<Contact> contactsFromApi = new ArrayList<>();
        contactsFromApi.add(new Contact(null, "", "Sincere",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona"));
        contactsFromApi.add(new Contact(2L, "Jane Smith", "jane@example.com",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona"));

        List<Contact> contactsFromDb = new ArrayList<>();
        contactsFromDb.add(new Contact(3L, "Alice Johnson", "alice@example.com",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona"));

        when(contactClientImpl.getAllContacts()).thenReturn(contactsFromApi);
        when(contactRepository.findAll()).thenReturn(contactsFromDb);

        List<Contact> resultContacts = contactService.getContacts();

        assertEquals(3, resultContacts.size());
        assertEquals(contactsFromApi.get(0), resultContacts.get(1));
        assertEquals(contactsFromApi.get(1), resultContacts.get(2));
        assertEquals(contactsFromDb.get(0), resultContacts.get(0));
    }

    @Test
    public void testWhenNoContactsReturnedFormRemote_returnAllGetContactsFromDB() {
        List<Contact> contactsFromDb = new ArrayList<>();
        contactsFromDb.add(new Contact(1L, "", "Sincere",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona"));
        contactsFromDb.add(new Contact(2L, "Jane Smith", "jane@example.com",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona"));

        List<Contact> contactsFromApi = new ArrayList<>();

        when(contactClientImpl.getAllContacts()).thenReturn(contactsFromApi);
        when(contactRepository.findAll()).thenReturn(contactsFromDb);

        List<Contact> resultContacts = contactService.getContacts();

        assertEquals(2, resultContacts.size());
    }

    @Test
    public void testWhenCalledGetById_returnsCorrectEntity() {
        Contact contactFormApi = new Contact(1L, "", "Sincere@gmail",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");
        when(contactClientImpl.getContactById(1L)).thenReturn(contactFormApi);

        Contact resultContact = contactService.getContactById(1L);

        assertEquals("Sincere@gmail", resultContact.getEmail());
    }

    @Test
    public void testWhenExternalApiReturnsNoData_returnsCorrectDataFromDB() {
        Contact contactFormDB = new Contact(1L, "", "Sincere@gmail",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");
        when(contactClientImpl.getContactById(1L)).thenReturn(null);
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contactFormDB));

        Contact resultContact = contactService.getContactById(1L);

        assertEquals("Sincere@gmail", resultContact.getEmail());
    }

    @Test
    public void testWhenCalledGetByIdWithWrongId_throwsNotFoundException() {
        Long nonExistentId = 999L;
        when(contactClientImpl.getContactById(nonExistentId)).thenReturn(null);
        when(contactRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contactService.getContactById(nonExistentId));
    }

    @Test
    public void whenCalledFilter_returnFilteredContacts() {
        List<Contact> contactsFromApi = new ArrayList<>();
        contactsFromApi.add(new Contact(1L, "", "Sincere",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona"));
        contactsFromApi.add(new Contact(2L, "Jane Smith", "jane@example.com",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona"));
        when(contactClientImpl.getAllContacts()).thenReturn(contactsFromApi);

        List<Contact> contactsFromDb = new ArrayList<>();
        contactsFromDb.add(new Contact(1L, "Sincere", "Sincere",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona"));

        ContactFilter filter = ContactFilter.builder().name("Sincere").build();

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Contact.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Contact.class)).thenReturn(root);
        when(typedQuery.getResultList()).thenReturn(contactsFromDb);
        when(contactService.getTypedQueryByFilter(filter)).thenReturn(typedQuery);

        List<Contact> result = contactService.getContactsByFilter(filter);
        assertEquals(contactsFromDb.size(), result.size());
    }

    @Test
    public void testWhenCalledCreateContactWithNewContact_saveAndReturnContact() {
        Contact contact = new Contact(null, "Sincere", "Sincere@mail.com",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");
        when(contactRepository.findByEmailAndCompanyName(anyString(), anyString())).thenReturn(Optional.empty());
        when(contactClientImpl.createContact(contact)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);

        Contact savedContact = contactService.createContact(contact);

        assertNotNull(savedContact);
        assertEquals(contact, savedContact);
    }

    @Test
    public void testWhenCalledCreateContactWithExistingContact_throwsError() {
        Contact contact = new Contact(1L, "Sincere", "Sincere@mail.com",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");
        when(contactRepository.findByEmailAndCompanyName(anyString(), anyString())).thenReturn(Optional.of(contact));

        assertThrows(IllegalArgumentException.class, () -> contactService.createContact(contact));
    }

    @Test
    public void testWhenCalledCreateFailsRemotely_throwsException() {
        Contact contact = new Contact(null, "Sincere", "Sincere@mail.com",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");
        when(contactRepository.findByEmailAndCompanyName(anyString(), anyString())).thenReturn(Optional.empty());
        when(contactClientImpl.createContact(contact)).thenThrow(new RuntimeException());

        assertThrows(ApiHandleException.class, () -> contactService.createContact(contact));
    }

    @Test
    public void whenCalledUpdate_updateAndReturnContact() {
        Contact contact = new Contact(1L, "Sincere", "Sincere@mail.com",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");
        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));
        when(contactClientImpl.updateContact(contact)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);

        Contact updateContact = contactService.updateContact(contact);

        assertNotNull(updateContact);
        assertEquals(contact, updateContact);

    }

    @Test
    public void whenCalledUpdateFailsRemotely_throwsException() {
        Contact contact = new Contact(1L, "Sincere", "Sincere@mail.com",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");
        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));
        when(contactClientImpl.updateContact(contact)).thenThrow(new RuntimeException());

        assertThrows(ApiHandleException.class, () -> contactService.updateContact(contact));
    }

    @Test
    public void whenCalledUpdateWithNonExistingContact_throwsException() {
        Contact contact = new Contact(100L, "Sincere", "Sincere@mail.com",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");
        when(contactRepository.findById(contact.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contactService.updateContact(contact));
    }
}
