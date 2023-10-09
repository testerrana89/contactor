package com.vrealcompany.contacthub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vrealcompany.contacthub.model.Contact;
import com.vrealcompany.contacthub.model.filter.ContactFilter;
import com.vrealcompany.contacthub.repository.UserRepository;
import com.vrealcompany.contacthub.service.ContactServiceImpl;
import com.vrealcompany.contacthub.service.auth.UserService;
import com.vrealcompany.contacthub.configuration.JwtAuthenticationFilter;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = { ContactController.class })
@AutoConfigureMockMvc(addFilters = false)
public class ContactControllerTest {

    @MockBean
    private ContactServiceImpl contactService;

    @MockBean
    private JwtAuthenticationFilter filter;

    @MockBean
    private UserService service;

    @MockBean
    private UserRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testWhenCalledGetContacts_returnsAllContacts() throws Exception {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1L, "Leanne Graham", "Sincere@april.biz",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona"));
        contacts.add(new Contact(2L, "Joanne Busgth", "joanne@april.biz",
                "1-885-736-5551 x56442", "TennisonDosch.org", "TD-Bank"));
        when(contactService.getContacts()).thenReturn(contacts);

        mockMvc.perform(get("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email", is("Sincere@april.biz")));
    }

    @Test
    void testWhenCalledGetContactById_returnsContactMatchingId() throws Exception {
        Contact contact = new Contact(1L, "Leanne Graham", "Sincere@april.biz",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");
        when(contactService.getContactById(1L)).thenReturn(contact);

        mockMvc.perform(get("/api/contacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("email", is("Sincere@april.biz")));
    }

    @Test
    void testWhenCalledGetContactByFilters_returnsCorrectContacts() throws Exception {
        ContactFilter filter = ContactFilter.builder().name("Le").build();
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1L, "Leanne Graham", "Sincere@april.biz",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona"));
        contacts.add(new Contact(2L, "Lenny Busgth", "joanne@april.biz",
                "1-885-736-5551 x56442", "TennisonDosch.org", "TD-Bank"));
        when(contactService.getContactsByFilter(filter)).thenReturn(contacts);

        mockMvc.perform(get("/api/contacts/filter?name=Le")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email", is("Sincere@april.biz")));
    }

    @Test
    void testWhenCalledCreateContactWithPayload_createANewContact() throws Exception {
        Contact newContact = new Contact(null, "Leanne Graham", "Sincere@april.biz",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");
        Contact savedContact = new Contact(1L, "Leanne Graham", "Sincere@april.biz",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");

        when(contactService.createContact(any())).thenReturn(savedContact);

        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newContact))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("Sincere@april.biz")));
    }

    @Test
    void testWhenCalledCreateContactWithErrors_returnValidationErrors() throws Exception {
        Contact newContact = new Contact(null, "", "Sincere",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");

        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newContact))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("must not be blank"))
                .andExpect(jsonPath("$.email").value("Email is not valid"));
    }

    @Test
    void testWhenCalledUpdateContactWithPayload_updateCorrectContact() throws Exception {
        Long contactId = 1L;
        Contact existingContact = new Contact(contactId, "Leanne Graham", "Sincere@april.biz",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");

        when(contactService.updateContact(any())).thenReturn(existingContact);

        mockMvc.perform(put("/api/contacts/{id}", contactId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingContact))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("Sincere@april.biz")));
    }

    @Test
    void testWhenThrownApiError_returnCorrectErrorResponse() throws Exception {
        Contact contact = new Contact(null, "Leanne Graham", "Sincere@april.biz",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");
        when(contactService.createContact(contact)).thenThrow(new ApiHandleException("Error Creating Contact"));

        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact))
                )
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message").value("Error Creating Contact"));
    }

    @Test
    void testWhenThrownEntityNotFoundError_returnCorrectErrorResponse() throws Exception {
        when(contactService.getContactById(any())).thenThrow(new EntityNotFoundException("No Contacts Found."));

        mockMvc.perform(get("/api/contacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No Contacts Found."));
    }

    @Test
    void testWhenThrownIllegalArgumentError_returnCorrectErrorResponse() throws Exception {
        Contact contact = new Contact(null, "Leanne Graham", "Sincere@april.biz",
                "1-770-736-8031 x56442", "hildegard.org", "Romaguera-Crona");
        when(contactService.createContact(contact)).thenThrow(new IllegalArgumentException("Contact already exists."));

        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Contact already exists."));
    }
}
