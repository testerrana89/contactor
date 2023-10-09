package com.vrealcompany.contacthub.service.client;

import com.vrealcompany.contacthub.event.ContactReceivedEvent;
import com.vrealcompany.contacthub.model.Contact;
import com.vrealcompany.contacthub.model.dto.CompanyResponse;
import com.vrealcompany.contacthub.model.dto.ContactResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ContactClientImpl implements ContactClient {

    Logger logger = LoggerFactory.getLogger(ContactClientImpl.class);

    @Autowired
    WebClient webClient;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    static final String USER_URL = "/users";

    @Cacheable(value = "contacts")
    @Override
    public List<Contact> getAllContacts() {
        logger.info("Retrieving Contacts from partner api");
        List<Contact> contacts =  webClient.get()
                .uri(USER_URL)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        clientResponse -> handleErrorResponse(clientResponse.statusCode()))
                .bodyToFlux(ContactResponse.class)
                .map(this::transformToContact)
                .collectList()
                .onErrorResume(throwable -> {
                    logger.error("Error occurred while retrieving contacts: {}", throwable.getMessage());
                    return Mono.just(Collections.emptyList());
                })
                .block();

        logger.info("Publishing Contact Received Event");
        eventPublisher.publishEvent(new ContactReceivedEvent(this, contacts));

        return contacts;
    }

    @Cacheable(value = "contacts", key = "#id")
    @Override
    public Contact getContactById(Long id) {
        logger.info("Retrieving Contact from partner api with id: {}", id);
        return webClient.get()
                .uri(USER_URL + "/" + id)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        clientResponse -> {
                            logger.error("Error retrieving contact from partner API with status code: "
                                    + clientResponse.statusCode());
                            return Mono.empty();
                        })
                .bodyToMono(ContactResponse.class)
                .map(this::transformToContact)
                .onErrorResume(throwable -> {
                    logger.error("Error occurred while retrieving contact: {}", throwable.getMessage());
                    return Mono.justOrEmpty(null);
                })
                .block();
    }

    @Override
    @CacheEvict(value = "contacts", allEntries = true)
    public Contact createContact(Contact contact) {
            ContactResponse payload = transformToResponse(contact);
            return webClient
                    .post()
                    .uri(USER_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(payload))
                    .retrieve()
                    .bodyToMono(ContactResponse.class)
                    .map(this::transformToContact)
                    .block();
    }

    @Override
    @CacheEvict(value = "contacts", key = "#contact.id")
    public Contact updateContact(Contact contact) {
            ContactResponse payload = transformToResponse(contact);
            return webClient
                    .put()
                    .uri(USER_URL + "/" + contact.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(payload))
                    .retrieve()
                    .bodyToMono(ContactResponse.class)
                    .map(this::transformToContact)
                    .block();
    }

    private Contact transformToContact(ContactResponse response) {
        return Contact.builder()
                .id((long) response.getId())
                .name(response.getName())
                .email(response.getEmail())
                .phone(response.getPhone())
                .website(response.getWebsite())
                .companyName(response.getCompany().getName())
                .build();
    }

    private ContactResponse transformToResponse(Contact contact) {
        return ContactResponse.builder()
                .name(contact.getName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .website(contact.getWebsite())
                .company(CompanyResponse.builder().name(contact.getCompanyName()).build()).build();
    }

    private Mono handleErrorResponse(HttpStatusCode statusCode) {
        logger.error("Error retrieving Contacts from partner api with status: {}", statusCode);
        return Mono.just(new ArrayList<>());
    }
}
