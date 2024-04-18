# Tuum Banking App
## Description

- App to **create** and **view accounts**, **create transactions** for an account for their corresponding currencies, **view transactions** filtered by account ID.
- Includes Account and Transaction services for these purposes.
- RabbitMQ console to listen to account and transaction creation messages.
- Includes integration tests for both services, with 100% method coverage, line coverage ~95%. 
-----


# How to run
- **Step 1**: `Run compose.yml`
- **Step 2**: Open the application in Docker to see everything working in harmony.
- **Step 3**: By default, app is running on 8080, RabbitMQ Console on 15672, DB on 5432.
-----

# Explanation of my important choices 

## DB Schema - went for three tables with a middle table for an account so that:
- Accounts: account_id; customer_id; country(Country code, 3 letter, e.g 'EST')
- Account_balances: account_balance_id; account_id; balance_id
- Balances: balance_id; available_amount; currency(Currency code, 3 letter, e.g 'EUR')
- Also a table for transactions
- Reasoning behind this was to make application more expandable, this way more easier to keep track of balances in case a user deciedes to create an account with a very high amount of different currencies.

## Creating separate service classes for Account-related and Transaction-related actions:
- AccountService deals with creating an account and finding an account by its ID, also calls out validation from validation class.
- TransactionService deals with creating a new transaction, updating account's balance and getting transactions for an account.
- Reasoning behind this decision was to keep account and transaction rules separate and make use of abstraction to make code more readable and easily expandable, e.g create new methods for various new account actions or so on.
-----

# Estimation of how many transactions application can handle on my machine
- Using transaction per second as a scale
- Using Postman - with 100 virtual users, running for 1 minute for a total of 5000 requests (75 tps) and an average response time of 16ms.
- Using account and transaction creation endpoints with random integers as ID's.
-----

# Scaling horizontally up a banking application such as this one:
- Ensuring that application/servers can handle any request at any time
- Avoiding local storage and using backups
- Load balancing to distribute incoming API requests evenly
- Breaking down application to smaller pieces, using distributed systems to run a part of a program each on a different machine
- More monitoring and testing
- Otherwise application structure is quite expandable and current packages can be used quite easily to scale the application
- Some little changes such as using UUID and creating more queries to repositories could also be useful for expansion.
 -----
 
# API Documentation

## POST /api/accounts/{accountId}/transactions

- Creates a new transaction for the specified account.

#### Parameters

- `accountId` (path): The ID of the account to create the transaction for.

#### Request Body

- `TransactionDTO` (required): The transaction data transfer object containing the details of the transaction.

#### Response

- `201 Created`: Returns the created `TransactionDTO` object.

#### Example

### POST /api/accounts/12345/transactions

- Content-Type: application/json
  
```json
{
  "amount": 1500.00,
  "currency": "USD",
  "direction": "OUT",
  "description": "Payment for services"
}
```
## GET /api/accounts/{accountId}/transactions

- Retrieves all transactions for the specified account.

#### Parameters

- `accountId` (path): The ID of the account to get the transactions for.

#### Response

- `200 OK`: Returns a list of TransactionDTO objects representing the transactions for the account.

#### Example

### GET /api/accounts/12345/transactions/


## POST /api/accounts/create

Creates a new account based on the provided details.

#### Request Body

- `AccountDTO` (required): The account data transfer object containing the details for account creation.

#### Response

- `201 Created`: Returns the created `Account` object.

#### Example

### POST /api/accounts/create

- Content-Type: application/json

```json
{
  "customerId": 12345,
  "country": "EST",
  "currency": ["USD", "EUR"]
}
```

## GET /api/accounts/{accountId}

Retrieves the details of an account by its ID.

#### Parameters

- `accountId` (path): The ID of the account to retrieve.

#### Response

- `200 OK`: Returns the `Account` object with given ID.

#### Example

### GET /api/accounts/12345/

-----
## Models

### TransactionDTO
- Represents a transaction with the following properties:

- `accountId`: The ID of the account associated with the transaction.
- `amount`: The amount of the transaction.
- `currency`: The currency of the transaction.
- `direction`: The direction of the transaction ("IN" or "OUT").
- `description`: A description of the transaction.

### AccountDTO
- Represents the data required to create an account with the following properties:

- `customerId`: The ID of the customer associated with the account.
- `country`: The country associated with the account.
- `currency`: A list of currencies associated with the account and their corresponding balances.
-----

## Error Handling

- If an error occurs, the API will return an appropriate HTTP status code along with an error message in the response body.



