INSERT INTO methods_of_payment (name) VALUES ('cash');
INSERT INTO methods_of_payment (name) VALUES ('debit card');

INSERT INTO currencies (symbol, exchange_rate) VALUES ('PLN', 1.0);
INSERT INTO currencies (symbol, exchange_rate) VALUES ('USD', 4.0);
INSERT INTO currencies (symbol, exchange_rate) VALUES ('EUR', 5.0);

INSERT INTO groups (name) VALUES ('family');
INSERT INTO groups (name) VALUES ('workers');
INSERT INTO groups (name) VALUES ('members-only');

INSERT INTO archived_groups (name) VALUES ('family2');
INSERT INTO archived_groups (name) VALUES ('workers2');

INSERT INTO preferences (currency_id, method_id) VALUES (1, 1);
INSERT INTO preferences (currency_id, method_id) VALUES (2, 2);
INSERT INTO preferences (currency_id, method_id) VALUES (3, 1);
INSERT INTO preferences (currency_id, method_id) VALUES (1, 2);

INSERT INTO users (name, surname, email, preferences_id, password) VALUES ('Herkules1', 'Herkules1', 'herkules1@gmail.com', 1, '123');
INSERT INTO users (name, surname, email, preferences_id, password) VALUES ('Herkules2', 'Herkules2', 'herkules2@gmail.com', 2, '234');
INSERT INTO users (name, surname, email, preferences_id, password) VALUES ('Herkules3', 'Herkules3', 'herkules3@gmail.com', 3, '345');
INSERT INTO users (name, surname, email, preferences_id, password) VALUES ('Herkules4', 'Herkules4', 'herkules4@gmail.com', 4, '456');

INSERT INTO roles (name) VALUES ('admin');
INSERT INTO roles (name) VALUES ('member');

INSERT INTO memberships (user_id, group_id, role_id) VALUES (1, 1, 1);
INSERT INTO memberships (user_id, group_id, role_id) VALUES (2, 1, 2);
INSERT INTO memberships (user_id, group_id, role_id) VALUES (3, 2, 2);
INSERT INTO memberships (user_id, group_id, role_id) VALUES (1, 2, 2);
INSERT INTO memberships (user_id, group_id, role_id) VALUES (2, 2, 1);
INSERT INTO memberships (user_id, group_id, role_id) VALUES (1, 3, 2);

INSERT INTO categories (name) VALUES ('food');
INSERT INTO categories (name) VALUES ('transport');

INSERT INTO expenses (title, price, expense_date, membership_id, category_id, method_id, currency_id)
    VALUES ('lunch', 50, '2024-10-10', 1, 1, 1, 1);
INSERT INTO expenses (title, price, expense_date, membership_id, category_id, method_id, currency_id)
    VALUES ('dinner', 100, '2025-11-30', 1, 1, 1, 1);
INSERT INTO expenses (title, price, expense_date, membership_id, category_id, method_id, currency_id)
    VALUES ('train-ticket', 200, '2024-12-22', 2, 2, 2, 2);
INSERT INTO expenses (title, price, membership_id, category_id, method_id, currency_id)
    VALUES ('groceries', 300, 3, 1, 1, 1);
INSERT INTO expenses (title, price, membership_id, category_id, method_id, currency_id)
    VALUES ('fast-food', 300, 3, 1, 2, 3);

INSERT INTO refresh_tokens (token, user_id) VALUES ('token1', 1);
INSERT INTO refresh_tokens (token, user_id) VALUES ('token2', 2);