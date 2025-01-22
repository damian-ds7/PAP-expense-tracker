-- Compile triggers before inserting data

/*
Trigger to archive deleted groups
 */
CREATE OR REPLACE TRIGGER archive_group
BEFORE DELETE ON groups
FOR EACH ROW
DECLARE
v_count NUMBER;
BEGIN
SELECT COUNT(*) INTO v_count
FROM archived_groups
WHERE id = :OLD.id;

IF v_count = 0 THEN
       INSERT INTO archived_groups (id, name)
       VALUES (:OLD.id, :OLD.name);
END IF;

INSERT INTO archived_memberships (id, group_id, user_id, role_id)
SELECT base_membership_seq.NEXTVAL, m.group_id, m.user_id, m.role_id
FROM memberships m
WHERE m.group_id = :OLD.id;

DELETE FROM memberships
WHERE group_id = :OLD.id;
END;
/



/*
Trigger to archive deleted memberships
 */
CREATE OR REPLACE TRIGGER archive_membership
    BEFORE DELETE ON memberships
    FOR EACH ROW
DECLARE
v_grp_name groups.name%TYPE;
    v_count     NUMBER;
BEGIN
SELECT COUNT(*) INTO v_count
FROM archived_groups
WHERE id = :OLD.group_id;

IF v_count = 0 THEN
SELECT g.name INTO v_grp_name
FROM groups g
WHERE g.id = :OLD.group_id;

INSERT INTO archived_groups (id, name)
VALUES (:OLD.group_id, v_grp_name);
END IF;

INSERT INTO archived_memberships (id, group_id, user_id, role_id)
VALUES (base_membership_seq.NEXTVAL,
        :OLD.group_id,
        :OLD.user_id,
        :OLD.role_id);
END;
/


/*
Trigger to fill creation_date for newly created user if empty
 */
CREATE OR REPLACE TRIGGER FILL_CREATION_DATE
    BEFORE INSERT
    ON USERS
    FOR EACH ROW
    WHEN (NEW.CREATION_DATE IS NULL)
BEGIN
    :NEW.CREATION_DATE := TO_DATE(SYSDATE);
END;
/
--------------------------------------------------------

INSERT INTO CATEGORIES (NAME)
VALUES ('Electronics');
INSERT INTO CATEGORIES (NAME)
VALUES ('Clothing');
INSERT INTO CATEGORIES (NAME)
VALUES ('Sports');
INSERT INTO CATEGORIES (NAME)
VALUES ('Furniture');
INSERT INTO CATEGORIES (NAME)
VALUES ('Books');
INSERT INTO CATEGORIES (NAME)
VALUES ('Leisure');
INSERT INTO CATEGORIES (NAME)
VALUES ('Food');
INSERT INTO CATEGORIES (NAME)
VALUES ('Healthcare');
INSERT INTO CATEGORIES (NAME)
VALUES ('Transport');
INSERT INTO CATEGORIES (NAME)
VALUES ('Garden');
INSERT INTO CATEGORIES (NAME)
VALUES ('Bills');
INSERT INTO CATEGORIES (NAME)
VALUES ('Other');
COMMIT;

--------------------------------------------------------

INSERT INTO CURRENCIES (EXCHANGE_RATE, SYMBOL)
VALUES (1.0, 'PLN'); -- Base currency
INSERT INTO CURRENCIES (EXCHANGE_RATE, SYMBOL)
VALUES (4.0, 'USD'); -- 1 USD = 4 PLN
INSERT INTO CURRENCIES (EXCHANGE_RATE, SYMBOL)
VALUES (4.5, 'EUR'); -- 1 EUR = 4.5 PLN
INSERT INTO CURRENCIES (EXCHANGE_RATE, SYMBOL)
VALUES (5.2, 'GBP'); -- 1 GBP = 5.2 PLN
INSERT INTO CURRENCIES (EXCHANGE_RATE, SYMBOL)
VALUES (3.1, 'CAD'); -- 1 CAD = 3.1 PLN
INSERT INTO CURRENCIES (EXCHANGE_RATE, SYMBOL)
VALUES (0.035, 'JPY'); -- 1 JPY = 0.035 PLN
INSERT INTO CURRENCIES (EXCHANGE_RATE, SYMBOL)
VALUES (2.8, 'AUD'); -- 1 AUD = 2.8 PLN
INSERT INTO CURRENCIES (EXCHANGE_RATE, SYMBOL)
VALUES (4.3, 'CHF'); -- 1 CHF = 4.3 PLN
INSERT INTO CURRENCIES (EXCHANGE_RATE, SYMBOL)
VALUES (0.056, 'INR'); -- 1 INR = 0.056 PLN
INSERT INTO CURRENCIES (EXCHANGE_RATE, SYMBOL)
VALUES (0.5, 'HKD'); -- 1 HKD = 0.5 PLN
INSERT INTO CURRENCIES (EXCHANGE_RATE, SYMBOL)
VALUES (3.4, 'SGD'); -- 1 SGD = 3.4 PLN
COMMIT;

--------------------------------------------------------

INSERT INTO GROUPS (ID, NAME)
VALUES (BASE_GROUP_SEQ.NEXTVAL, 'Roommates');
INSERT INTO GROUPS (ID, NAME)
VALUES (BASE_GROUP_SEQ.NEXTVAL, 'Family');
INSERT INTO GROUPS (ID, NAME)
VALUES (BASE_GROUP_SEQ.NEXTVAL, 'Travel Buddies');
INSERT INTO GROUPS (ID, NAME)
VALUES (BASE_GROUP_SEQ.NEXTVAL, 'Project Team');
INSERT INTO GROUPS (ID, NAME)
VALUES (BASE_GROUP_SEQ.NEXTVAL, 'Friends Gathering');

-- Groups to be deleted
INSERT INTO GROUPS (ID, NAME)
VALUES (BASE_GROUP_SEQ.NEXTVAL, 'Old Roommates');
INSERT INTO GROUPS (ID, NAME)
VALUES (BASE_GROUP_SEQ.NEXTVAL, 'Previous Family');
INSERT INTO GROUPS (ID, NAME)
VALUES (BASE_GROUP_SEQ.NEXTVAL, 'Past Travel Buddies');

COMMIT;

--------------------------------------------------------

INSERT INTO METHODS_OF_PAYMENT (NAME)
VALUES ('card');
INSERT INTO METHODS_OF_PAYMENT (NAME)
VALUES ('cash');
INSERT INTO METHODS_OF_PAYMENT (NAME)
VALUES ('crypto');
COMMIT;

--------------------------------------------------------

INSERT INTO ROLES (NAME)
VALUES ('member');
INSERT INTO ROLES (NAME)
VALUES ('viewer');
INSERT INTO ROLES (NAME)
VALUES ('admin');
INSERT INTO ROLES (NAME)
VALUES ('editor');
COMMIT;

--------------------------------------------------------

-- User 1 (John Doe)
INSERT INTO PREFERENCES (CURRENCY_ID, METHOD_ID, LANGUAGE)
VALUES (1, 1, 'English');
-- PLN, Card, English

-- User 2 (Jane Smith)
INSERT INTO PREFERENCES (CURRENCY_ID, METHOD_ID, LANGUAGE)
VALUES (1, 2, 'English');
-- PLN, Cash, English

-- User 3 (Michael Brown)
INSERT INTO PREFERENCES (CURRENCY_ID, METHOD_ID, LANGUAGE)
VALUES (2, 3, 'English');
-- USD, Crypto, English

-- User 4 (Emily Jones)
INSERT INTO PREFERENCES (CURRENCY_ID, METHOD_ID, LANGUAGE)
VALUES (1, 1, 'English');
-- PLN, Card, English

-- User 5 (Daniel Wilson)
INSERT INTO PREFERENCES (CURRENCY_ID, METHOD_ID, LANGUAGE)
VALUES (1, 2, 'English');
-- PLN, Cash, English

-- User 6 (Sophia Taylor)
INSERT INTO PREFERENCES (CURRENCY_ID, METHOD_ID, LANGUAGE)
VALUES (2, 1, 'English');
-- USD, Card, English

-- User 7 (Liam Anderson)
INSERT INTO PREFERENCES (CURRENCY_ID, METHOD_ID, LANGUAGE)
VALUES (1, 3, 'English');
-- PLN, Crypto, English

-- User 8 (Olivia Martin)
INSERT INTO PREFERENCES (CURRENCY_ID, METHOD_ID, LANGUAGE)
VALUES (3, 2, 'English');
-- EUR, Cash, English

-- User 9 (Noah Moore)
INSERT INTO PREFERENCES (CURRENCY_ID, METHOD_ID, LANGUAGE)
VALUES (3, 1, 'English');
-- EUR, Card, English

-- User 10 (Ava Thomas)
INSERT INTO PREFERENCES (CURRENCY_ID, METHOD_ID, LANGUAGE)
VALUES (1, 2, 'English'); -- PLN, Cash, English

COMMIT;

--------------------------------------------------------


INSERT INTO USERS (CREATION_DATE, PREFERENCES_ID, EMAIL, NAME, PASSWORD, SURNAME)
VALUES (TO_DATE('2025-01-15', 'YYYY-MM-DD'), 1, 'john.doe@example.com', 'John', 'password123', 'Doe');

INSERT INTO USERS (CREATION_DATE, PREFERENCES_ID, EMAIL, NAME, PASSWORD, SURNAME)
VALUES (TO_DATE('2025-01-16', 'YYYY-MM-DD'), 2, 'jane.smith@example.com', 'Jane', 'securepass456', 'Smith');

INSERT INTO USERS (PREFERENCES_ID, EMAIL, NAME, PASSWORD, SURNAME)
VALUES (3, 'michael.brown@example.com', 'Michael', 'mypassword789', 'Brown');

INSERT INTO USERS (CREATION_DATE, PREFERENCES_ID, EMAIL, NAME, PASSWORD, SURNAME)
VALUES (TO_DATE('2025-01-18', 'YYYY-MM-DD'), 4, 'emily.jones@example.com', 'Emily', 'emilypassword', 'Jones');

INSERT INTO USERS (CREATION_DATE, PREFERENCES_ID, EMAIL, NAME, PASSWORD, SURNAME)
VALUES (TO_DATE('2025-01-19', 'YYYY-MM-DD'), 5, 'daniel.wilson@example.com', 'Daniel', 'danielpass123', 'Wilson');

INSERT INTO USERS (CREATION_DATE, PREFERENCES_ID, EMAIL, NAME, PASSWORD, SURNAME)
VALUES (TO_DATE('2025-01-20', 'YYYY-MM-DD'), 6, 'sophia.taylor@example.com', 'Sophia', 'sophiasecure789', 'Taylor');

INSERT INTO USERS (CREATION_DATE, PREFERENCES_ID, EMAIL, NAME, PASSWORD, SURNAME)
VALUES (TO_DATE('2025-01-21', 'YYYY-MM-DD'), 7, 'liam.anderson@example.com', 'Liam', 'liamspassword', 'Anderson');

INSERT INTO USERS (CREATION_DATE, PREFERENCES_ID, EMAIL, NAME, PASSWORD, SURNAME)
VALUES (TO_DATE('2025-01-22', 'YYYY-MM-DD'), 8, 'olivia.martin@example.com', 'Olivia', 'oliviapass456', 'Martin');

INSERT INTO USERS (PREFERENCES_ID, EMAIL, NAME, PASSWORD, SURNAME)
VALUES (9, 'noah.moore@example.com', 'Noah', 'noahsecure123', 'Moore');

INSERT INTO USERS (PREFERENCES_ID, EMAIL, NAME, PASSWORD, SURNAME)
VALUES (10, 'ava.thomas@example.com', 'Ava', 'avapass789', 'Thomas');

COMMIT;

--------------------------------------------------------

-- John Doe: Roommates (member) and Family (admin)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (1, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 1); -- Roommates (member)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (2, BASE_MEMBERSHIP_SEQ.NEXTVAL, 3, 1);
-- Family (admin)

-- Jane Smith: Roommates (viewer) and Travel Buddies (member)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (1, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 2); -- Roommates (viewer)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (3, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 2);
-- Travel Buddies (member)

-- Michael Brown: Project Team (admin) and Friends Gathering (viewer)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (4, BASE_MEMBERSHIP_SEQ.NEXTVAL, 3, 3); -- Project Team (admin)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (5, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 3);
-- Friends Gathering (viewer)

-- Emily Jones: Travel Buddies (editor) and Friends Gathering (member)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (3, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 4); -- Travel Buddies (editor)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (5, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 4);
-- Friends Gathering (member)

-- Daniel Wilson: Roommates (member) and Family (viewer)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (1, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 5); -- Roommates (member)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (2, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 5);
-- Family (viewer)

-- Sophia Taylor: Project Team (editor) and Roommates (admin)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (4, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 6); -- Project Team (editor)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (1, BASE_MEMBERSHIP_SEQ.NEXTVAL, 3, 6);
-- Roommates (admin)

-- Liam Anderson: Travel Buddies (viewer) and Project Team (member)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (3, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 7); -- Travel Buddies (viewer)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (4, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 7);
-- Project Team (member)

-- Olivia Martin: Roommates (member) and Family (admin)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (1, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 8); -- Roommates (member)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (2, BASE_MEMBERSHIP_SEQ.NEXTVAL, 3, 8);
-- Family (admin)

-- Noah Moore: Friends Gathering (member) and Travel Buddies (admin)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (5, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 9); -- Friends Gathering (member)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (3, BASE_MEMBERSHIP_SEQ.NEXTVAL, 3, 9);
-- Travel Buddies (admin)

-- Ava Thomas: Project Team (viewer) and Roommates (editor)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (4, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 10); -- Project Team (viewer)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (1, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 10);
-- Roommates (editor)

-- Memberships that will be deleted

-- John Doe: Old Roommates (member) and Previous Family (admin)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (6, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 1); -- Old Roommates (member)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (7, BASE_MEMBERSHIP_SEQ.NEXTVAL, 3, 1);
-- Previous Family (admin)

-- Jane Smith: Old Roommates (viewer) and Past Travel Buddies (member)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (6, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 2); -- Old Roommates (viewer)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (8, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 2);
-- Past Travel Buddies (member)

-- Michael Brown: Previous Family (admin) and Past Travel Buddies (viewer)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (7, BASE_MEMBERSHIP_SEQ.NEXTVAL, 3, 3); -- Previous Family (admin)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (8, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 3);
-- Past Travel Buddies (viewer)

-- Emily Jones: Past Travel Buddies (editor) and Previous Family (member)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (8, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 4); -- Past Travel Buddies (editor)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (7, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 4);
-- Previous Family (member)

-- Daniel Wilson: Old Roommates (member) and Past Travel Buddies (viewer)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (6, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 5); -- Old Roommates (member)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (8, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 5);
-- Past Travel Buddies (viewer)

-- Sophia Taylor: Previous Family (editor) and Old Roommates (admin)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (7, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 6); -- Previous Family (editor)
INSERT INTO MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, USER_ID)
VALUES (6, BASE_MEMBERSHIP_SEQ.NEXTVAL, 3, 6); -- Old Roommates (admin)

COMMIT;


--------------------------------------------------------

-- John Doe invites Jane Smith to join 'Roommates' as a 'member'
INSERT INTO TEMPORARY_MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, SENDER_ID, USER_ID)
VALUES (1, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 1, 2);

-- Michael Brown invites Emily Jones to join 'Family' as an 'admin'
INSERT INTO TEMPORARY_MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, SENDER_ID, USER_ID)
VALUES (2, BASE_MEMBERSHIP_SEQ.NEXTVAL, 3, 3, 4);

-- Emily Jones invites Daniel Wilson to join 'Travel Buddies' as a 'viewer'
INSERT INTO TEMPORARY_MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, SENDER_ID, USER_ID)
VALUES (3, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 4, 5);

-- Jane Smith invites Liam Anderson to join 'Roommates' as a 'member'
INSERT INTO TEMPORARY_MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, SENDER_ID, USER_ID)
VALUES (1, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 2, 7);

-- Daniel Wilson invites Olivia Martin to join 'Family' as a 'member'
INSERT INTO TEMPORARY_MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, SENDER_ID, USER_ID)
VALUES (2, BASE_MEMBERSHIP_SEQ.NEXTVAL, 1, 5, 8);

-- Sophia Taylor invites Noah Moore to join 'Travel Buddies' as an 'admin'
INSERT INTO TEMPORARY_MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, SENDER_ID, USER_ID)
VALUES (3, BASE_MEMBERSHIP_SEQ.NEXTVAL, 3, 6, 9);

-- John Doe invites Ava Thomas to join 'Project Team' as a 'viewer'
INSERT INTO TEMPORARY_MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, SENDER_ID, USER_ID)
VALUES (4, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 1, 10);

-- Michael Brown invites Sophia Taylor to join 'Friends Gathering' as a 'viewer'
INSERT INTO TEMPORARY_MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, SENDER_ID, USER_ID)
VALUES (5, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 3, 6);

-- Olivia Martin invites Liam Anderson to join 'Roommates' as a 'viewer'
INSERT INTO TEMPORARY_MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, SENDER_ID, USER_ID)
VALUES (1, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 8, 7);

-- Noah Moore invites Emily Jones to join 'Project Team' as an 'editor'
INSERT INTO TEMPORARY_MEMBERSHIPS (GROUP_ID, ID, ROLE_ID, SENDER_ID, USER_ID)
VALUES (4, BASE_MEMBERSHIP_SEQ.NEXTVAL, 2, 9, 4);

COMMIT;

--------------------------------------------------------

SET DEFINE OFF;

-- Roommates Group (Group ID: 1)
-- Expense 1: Rent Payment
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-15', 'YYYY-MM-DD'), 500.00, 11, 1, 1, 1, 'Rent');

-- Expense 2: Utilities Payment
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-16', 'YYYY-MM-DD'), 150.00, 12, 1, 1, 2, 'Utilities');

-- Expense 3: Grocery Shopping
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-17', 'YYYY-MM-DD'), 200.00, 7, 1, 1, 1, 'Grocery Shopping');

-- Family Group (Group ID: 2)
-- Expense 1: Family Dinner
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-15', 'YYYY-MM-DD'), 100.00, 6, 1, 2, 2, 'Family Dinner');

-- Expense 2: Movie Tickets
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-16', 'YYYY-MM-DD'), 80.00, 6, 1, 2, 1, 'Movie Tickets');

-- Expense 3: Shopping
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-17', 'YYYY-MM-DD'), 150.00, 7, 1, 2, 3, 'Shopping');

-- Travel Buddies Group (Group ID: 3)
-- Expense 1: Hotel Stay
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-15', 'YYYY-MM-DD'), 300.00, 6, 1, 3, 1, 'Hotel Stay');

-- Expense 2: Plane Tickets
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-16', 'YYYY-MM-DD'), 400.00, 9, 1, 3, 2, 'Plane Tickets');

-- Expense 3: Dinner at Local Restaurant
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-17', 'YYYY-MM-DD'), 120.00, 6, 1, 3, 1, 'Dinner at Local Restaurant');

-- Project Team Group (Group ID: 4)
-- Expense 1: Office Supplies
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-15', 'YYYY-MM-DD'), 50.00, 12, 1, 4, 3, 'Office Supplies');

-- Expense 2: Conference Fees
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-16', 'YYYY-MM-DD'), 250.00, 12, 1, 4, 1, 'Conference Fees');

-- Expense 3: Team Lunch
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-17', 'YYYY-MM-DD'), 90.00, 7, 1, 4, 2, 'Team Lunch');

-- Friends Gathering Group (Group ID: 5)
-- Expense 1: Party Supplies
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-15', 'YYYY-MM-DD'), 60.00, 6, 1, 5, 1, 'Party Supplies');

-- Expense 2: Snacks & Drinks
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-16', 'YYYY-MM-DD'), 80.00, 7, 1, 5, 2, 'Snacks & Drinks');

-- Expense 3: Event Tickets
INSERT INTO EXPENSES (EXPENSE_DATE, PRICE, CATEGORY_ID, CURRENCY_ID, MEMBERSHIP_ID, METHOD_ID, TITLE)
VALUES (TO_DATE('2025-01-17', 'YYYY-MM-DD'), 150.00, 6, 1, 5, 3, 'Event Tickets');

-- Commit the changes
COMMIT;

--------------------------------------------------------

DELETE
FROM GROUPS
WHERE NAME IN ('Old Roommates', 'Previous Family', 'Past Travel Buddies');
COMMIT;