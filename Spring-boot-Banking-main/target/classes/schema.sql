CREATE SCHEMA demo;

CREATE TABLE demo.account (
    id bigint NOT NULL PRIMARY KEY,
    account_number CHAR(8) NOT NULL,
    current_balance NUMERIC(10,3) NOT NULL,
    bank_name VARCHAR(50) NOT NULL,
    owner_name VARCHAR(50) NOT NULL,
    UNIQUE (account_number)
);

CREATE SEQUENCE demo.transaction_sequence START WITH 5;
CREATE TABLE demo.transaction (
    id bigint NOT NULL PRIMARY KEY,
    source_account_id bigint NOT NULL REFERENCES online_bank.account(id),
    target_account_id bigint NOT NULL REFERENCES online_bank.account(id),
    -- Partially denormalize for performance
    target_owner_name varchar(50) NOT NULL,
    amount NUMERIC(10,3) NOT NULL,
    initiation_date timestamp NOT NULL,
    completion_date TIMESTAMP,
    reference VARCHAR(255),
    transactionid bigint
);
