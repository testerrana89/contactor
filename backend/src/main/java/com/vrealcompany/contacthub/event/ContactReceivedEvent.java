package com.vrealcompany.contacthub.event;

import com.vrealcompany.contacthub.model.Contact;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class ContactReceivedEvent extends ApplicationEvent {

    List<Contact> contactsReceived;

    public ContactReceivedEvent(Object source, List<Contact> contactsReceived) {
        super(source);
        this.contactsReceived = contactsReceived;
    }

}
