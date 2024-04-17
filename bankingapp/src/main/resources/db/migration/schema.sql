-- Create schema for Accounts
CREATE TABLE IF NOT EXISTS accounts (
                                        account_id SERIAL PRIMARY KEY,
                                        customer_id BIGINT NOT NULL,
                                        country VARCHAR(3) NOT NULL
);

-- Create schema for Balances
CREATE TABLE IF NOT EXISTS balances (
                                        balance_id SERIAL PRIMARY KEY,
                                        available_amount NUMERIC(19, 4) NOT NULL,
                                        currency VARCHAR(3) NOT NULL
);

-- Create schema for Account_Balances
CREATE TABLE IF NOT EXISTS account_balances (
                                                account_balance_id SERIAL PRIMARY KEY,
                                                account_id BIGINT NOT NULL,
                                                balance_id BIGINT NOT NULL,
                                                CONSTRAINT fk_account
                                                    FOREIGN KEY(account_id)
                                                        REFERENCES accounts(account_id)
                                                        ON DELETE CASCADE,
                                                CONSTRAINT fk_balance
                                                    FOREIGN KEY(balance_id)
                                                        REFERENCES balances(balance_id)
                                                        ON DELETE CASCADE
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


