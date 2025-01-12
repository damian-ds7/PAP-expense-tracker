CREATE TABLE methods_of_payment (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR2(255) NOT NULL
);

CREATE TABLE currencies (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR2(255) NOT NULL,
    exchange_rate DOUBLE NOT NULL DEFAULT 1.0
);

CREATE TABLE groups (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR2(255) NOT NULL
);

CREATE TABLE archived_groups (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR2(255) NOT NULL
);

CREATE TABLE preferences (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    currency_id LONG NOT NULL,
    method_id LONG NOT NULL,
    language VARCHAR2(255) NOT NULL DEFAULT 'pl',
    FOREIGN KEY (currency_id) REFERENCES currencies(id),
    FOREIGN KEY (method_id) REFERENCES methods_of_payment(id)
);

CREATE TABLE users (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR2(255) NOT NULL,
    surname VARCHAR2(255) NOT NULL,
    email VARCHAR2(255) NOT NULL,
    creation_date DATE NOT NULL DEFAULT CURRENT_DATE,
    preferences_id LONG NOT NULL,
    password VARCHAR2(255) NOT NULL,
    FOREIGN KEY (preferences_id) REFERENCES preferences(id)
);

CREATE TABLE roles (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR2(255) NOT NULL
);

CREATE TABLE base_memberships (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    user_id LONG NOT NULL,
    group_id LONG NOT NULL,
    role_id LONG NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (group_id) REFERENCES groups(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE memberships (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    user_id LONG NOT NULL,
    group_id LONG NOT NULL,
    role_id LONG NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (group_id) REFERENCES groups(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE temporary_memberships (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    sender_id LONG NOT NULL,
    user_id LONG NOT NULL,
    group_id LONG NOT NULL,
    role_id LONG NOT NULL,
    FOREIGN KEY (sender_id) references users(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (group_id) REFERENCES groups(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE categories (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR2(255) DEFAULT 'food'
);

CREATE TABLE expenses (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR2(255) NOT NULL,
    price DOUBLE NOT NULL,
    expense_date DATE NOT NULL DEFAULT CURRENT_DATE,
    membership_id LONG NOT NULL,
    category_id LONG NOT NULL,
    method_id LONG NOT NULL,
    currency_id LONG NOT NULL,
    FOREIGN KEY (membership_id) REFERENCES memberships(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (method_id) REFERENCES methods_of_payment(id),
    FOREIGN KEY (currency_id) REFERENCES currencies(id)
);

CREATE TABLE refresh_tokens (
    id LONG AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR2(255) NOT NULL,
    user_id LONG NOT NULL,
    expiry_date TIMESTAMP
);