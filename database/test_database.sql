-- list of users with their prefered currency and method of payment

SELECT
    u.name AS user_name,
    u.surname AS user_surname,
    mop.name AS preferred_payment_method,
    c.symbol AS preferred_currency
FROM
    users u
JOIN
    preferences p ON u.preferences_id = p.id
JOIN
    methods_of_payment mop ON p.method_id = mop.id
JOIN
    currencies c ON p.currency_id = c.id;


-- list of expenses with their category, method of payment and user who made the expense

SELECT
    e.title AS expense_title,
    e.price AS expense_price,
    c.name AS category_name,
    u.name || ' ' || u.surname AS user_full_name,
    mop.name AS method_of_payment
FROM
    expenses e
JOIN
    categories c ON e.category_id = c.id
JOIN
    memberships m ON e.membership_id = m.id
JOIN
    users u ON m.user_id = u.id
JOIN
    methods_of_payment mop ON e.method_id = mop.id;

-- list of the most active groups based on the number of members

SELECT
    g.name AS group_name,
    COUNT(m.id) AS member_count
FROM
    groups g
LEFT JOIN
    memberships m ON g.id = m.group_id
GROUP BY
    g.name
ORDER BY
    member_count DESC;

-- expenses devided by categories and users

SELECT
    u.name || ' ' || u.surname AS user_full_name,
    c.name AS category_name,
    SUM(e.price) AS total_spent
FROM
    expenses e
JOIN
    categories c ON e.category_id = c.id
JOIN
    memberships m ON e.membership_id = m.id
JOIN
    users u ON m.user_id = u.id
GROUP BY
    u.name, u.surname, c.name
ORDER BY
    total_spent DESC;

-- users without assigned preferences

SELECT
    u.name AS user_name,
    u.surname AS user_surname
FROM
    users u
LEFT JOIN
    preferences p ON u.preferences_id = p.id
WHERE
    p.id IS NULL;

-- average expenses per category

SELECT
    c.name AS category_name,
    AVG(e.price) AS average_expense
FROM
    expenses e
JOIN
    categories c ON e.category_id = c.id
GROUP BY
    c.name
ORDER BY
    average_expense DESC;

-- users who spent the most in a given group

SELECT
    u.name || ' ' || u.surname AS user_full_name,
    SUM(e.price) AS total_spent
FROM
    expenses e
JOIN
    memberships m ON e.membership_id = m.id
JOIN
    users u ON m.user_id = u.id
WHERE
    m.group_id = 1
GROUP BY
    u.name, u.surname;

-- users who do not belong to any group

SELECT
    u.name AS user_name,
    u.surname AS user_surname
FROM
    users u
LEFT JOIN
    memberships m ON u.id = m.user_id
WHERE
    m.id IS NULL;

-- all expenses made in a given currency

SELECT
    e.title AS expense_title,
    e.price AS expense_price,
    c.symbol AS currency_symbol
FROM
    expenses e
JOIN
    currencies c ON e.currency_id = c.id
WHERE
    c.symbol = 'PLN';

-- users with the biggest number of unique roles in groups

SELECT
    u.name || ' ' || u.surname AS user_full_name,
    COUNT(DISTINCT m.role_id) AS role_count
FROM
    users u
JOIN
    memberships m ON u.id = m.user_id
GROUP BY
    u.name, u.surname
ORDER BY
    role_count DESC;

-- expenses devided by users and groups

SELECT
    g.name AS group_name,
    u.name || ' ' || u.surname AS user_full_name,
    SUM(e.price) AS total_spent
FROM
    expenses e
JOIN
    memberships m ON e.membership_id = m.id
JOIN
    groups g ON m.group_id = g.id
JOIN
    users u ON m.user_id = u.id
GROUP BY
    g.name, u.name, u.surname
ORDER BY
    g.name, total_spent DESC;










