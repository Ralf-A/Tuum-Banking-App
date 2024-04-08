-- Create schema for Accounts
CREATE TABLE IF NOT EXISTS accounts (
                                        account_id SERIAL PRIMARY KEY,
                                        customer_id BIGINT NOT NULL,
                                        country VARCHAR(3) NOT NULL
);

-- Create schema for Balances
CREATE TABLE IF NOT EXISTS balances (
                                        balance_id SERIAL PRIMARY KEY,
                                        account_id BIGINT NOT NULL,
                                        available_amount NUMERIC(19, 4) NOT NULL,
                                        currency VARCHAR(3) NOT NULL,
                                        CONSTRAINT fk_account
                                            FOREIGN KEY(account_id)
                                                REFERENCES accounts(account_id)
                                                ON DELETE CASCADE
);

-- Create schema for Customers
CREATE TABLE IF NOT EXISTS customers (
                                         customer_id SERIAL PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
                                         email VARCHAR(255) NOT NULL UNIQUE
);

-- Create schema for Transactions
CREATE TABLE IF NOT EXISTS transactions (
                                            transaction_id SERIAL PRIMARY KEY,
                                            account_id BIGINT NOT NULL,
                                            amount NUMERIC(19, 4) NOT NULL,
                                            currency VARCHAR(3) NOT NULL,
                                            direction VARCHAR(3) CHECK (direction IN ('IN', 'OUT')),
                                            description TEXT,
                                            balance_after_transaction NUMERIC(19, 4) NOT NULL,
                                            CONSTRAINT fk_account
                                                FOREIGN KEY(account_id)
                                                    REFERENCES accounts(account_id)
                                                    ON DELETE CASCADE
);

-- Indexes for faster search
CREATE INDEX idx_customer ON accounts(customer_id);
CREATE INDEX idx_account_balance ON balances(account_id);
CREATE INDEX idx_account_transaction ON transactions(account_id);
CREATE INDEX idx_customer_name ON customers(customer_id);

