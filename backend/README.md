
# Contact Hub Back End

## Run Locally

Clone the project

```bash
  git clone https://github.com/rumesha89/contact-hub.git
```

Go to the project directory

```bash
  cd contact-hub/backend
```

Run Springboot project

```bash
  mvn spring-boot:run
```


## API Reference

#### Get all contact
Returns all the contacts (Pagination to be implemented)

```http
  GET /api/contacts
```


#### Get contact

```http
  GET /api/contacts/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Long` | **Required**. Id of Contact to fetch |


#### Post contact

```http
  POST /api/contacts/
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `payload`      | `Contact` | **Required**. Valid instance of minimum of Contact with email, name, company name |


#### Edit contact

```http
  PUT /api/contacts/
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `Long` | **Required**. Id of Contact to update |
| `payload`      | `Contact` | **Required**. Valid instance of minimum of Contact with email, name, company name |


## Screenshots
<img width="1330" alt="Screen Shot 2023-10-03 at 1 50 24 PM" src="https://github.com/rumesha89/contact-hub/assets/60807037/1fcbffba-237c-4354-866e-9298a2682b0d">
<img width="514" alt="Screen Shot 2023-10-03 at 1 52 29 PM" src="https://github.com/rumesha89/contact-hub/assets/60807037/b7cd380c-3400-4d6a-9722-2f48e0b260c7">

<img width="1535" alt="Screen Shot 2023-10-03 at 1 54 03 PM" src="https://github.com/rumesha89/contact-hub/assets/60807037/20b7d173-0bb8-4d3a-8c2e-e2ebbedd52e1">


## Assumptions and User Stories

### User Story 1 - View All Existing Contacts
- Can accessed by a public unauthenticated user
- Only 10 contacts will be returned by the partner api
- Assuming partner api contact list can be updated by external parties
- Assuming the partner api has a moderate to low update frequency
- To minimize the network calls/3rd party api calls requests will be cached for a predefined time(2 minutes at the moment)
- Response coming from partner api will be updated in database asynchronously
- Since partner api is returning only 10 will be combining with any contacts exists in the database
- In case partner api is returning an error, contacts from database will be returned

### User Story 2 - Filter Contacts
- Can accessed by a public unauthenticated user
- Only filterable by name
- Partner api is cached to minimize repeated calls 
- Will return a merged list from api and database to minimize stale data

### User Story 3 - Create Contacts
- Can accessed by a public unauthenticated user
- Will be sharing with the partner by calling their POST api
- Payload will be validated
    - Email correct format
    - Name should not be blank
    - Company Name should not be blank
    - Email should not be blank
- Validated custom error messages will be sent in error response
- If a user exists with the same email, will return an error with a detailed message
- Will try to create in partner if success will be saved in the database
- If unsuccessful will return an Exception with a detailed message

### User Story 4 - Update Contacts
- Cannot be accessed by a public unauthenticated user
- Have to call with a token obtained from /login api in the header
- Can only edit name, website and phone number at the moment
- If the contact requested is not present in remote, will accessed in database
- If not in database too, will return entity not found exception with a detailed error message
- Contact entity validations will apply to request body


## Test Scenarios

* Have written api tests, service layer tests for below scenarios

- when get All contacts returns all the contacts merged from partner and database
- when get All contacts if partner api fails send all from database
- when get All contacts if datanase contacts empty send all from partner
- when get All contacts nothing in partner and Database return empty list
- when get contact by id send correct contact for that id
- when get contact by id if partner api fails send contact from database
- when get contact by id if partner api sends empty then send contact from database
- when get contact by id if a contact not present under that id send EntityNotFound
- when create contact then create in partner api then database
- when create contact if partner api fails then return error
- when create contact if already in the system return error
- when create contact if validations fails send error with error description
- when update contact then update in partner api then database
- when update contact if partner api fails then return error
- when update contact if not found in the system return error
- when update contact if validations fails send error with error description

- test third party client 
    - convert to correct entity and to request
    - handle errors from 3rd part api
