
# Contact Hub

Contact management system

## Run Project

Clone the project

```bash
  git clone https://github.com/rumesha89/contact-hub.git
```

Go to the project directory

```bash
  cd contact-hub
```

Run Docker comman

```bash
  docker-compose up
```

## User to login

email: rumesha@mail.com
password: password


## Sonar Cloud Report
https://sonarcloud.io/summary/new_code?id=rumesha_contact-app-backend&branch=main

* Other coverage and tests reports can be found in [Back-end Documentation](https://github.com/rumesha89/contact-hub/blob/feature/backend-init/backend/README.md)


## Assumptions and User Stories
- Partner Api sending only 10 contacts only, regardless how many created
- Partner Api update frequency is low to mid
- Partner contact list can be edited/added by other parties
- Partner Api list is the higher source of thruth so periodically will be synced with our system
- Create Api sends a created response, but not getting persisted in Partner(Partner API major limitation)
    - So for testing purposes create only one at the moment.
- Edit also sends a success response but not getting persisted, 
    - So in subsequent cycles it will overwrite database


## Screenshots

Please refer to Documentation


## Documentation
[Back-end Assumptions, Test cases and Documentation](https://github.com/rumesha89/contact-hub/blob/feature/backend-init/backend/README.md)

[Front-end Assumptions, Test cases and Documentation](https://github.com/rumesha89/contact-hub/blob/feature/backend-init/frontend/README.md)


