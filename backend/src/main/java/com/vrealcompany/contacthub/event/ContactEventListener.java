package com.vrealcompany.contacthub.event;

import com.vrealcompany.contacthub.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ContactEventListener {
    @Autowired
    private ContactService contactService;

    @Async
    @EventListener
    public void handleContactsReceivedEvent(ContactReceivedEvent event) {
        contactService.createContacts(event.contactsReceived);
    }
}
