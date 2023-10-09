package com.vrealcompany.contacthub.configuration;

import com.vrealcompany.contacthub.service.client.ContactClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class InitialSyncConfiguration implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    ContactClient client;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        client.getAllContacts();
    }
}
