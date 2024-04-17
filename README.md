# Tuum Banking App

# How to run
-----
# Explanation of my important choices 
-----
# Estimation of how many transactions application can handle on my machine
-----
# Scaling app horizontally
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

## Error Handling

- If an error occurs, the API will return an appropriate HTTP status code along with an error message in the response body.



