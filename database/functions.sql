CREATE OR REPLACE FUNCTION EXPENSE_PRICE_TO_PLN(
    PRICE NUMBER,
    CURRENCY_ID NUMBER
) RETURN NUMBER AS
    RATE NUMBER;
BEGIN
    SELECT EXCHANGE_RATE
    INTO RATE
    FROM CURRENCIES
    WHERE ID = CURRENCY_ID;
    RETURN PRICE * RATE;
END;
/

CREATE OR REPLACE FUNCTION MOST_SPENDING_USER_IN_GROUP(
    G_ID NUMBER
) RETURN NUMBER AS
    V_USER_ID  NUMBER := -1;
    V_SPENDING NUMBER;
BEGIN
    SELECT M.USER_ID,
           SUM(EXPENSE_PRICE_TO_PLN(
                   E.PRICE,
                   E.CURRENCY_ID
               )) AS TOTALSPENDING
    INTO
        V_USER_ID,
        V_SPENDING
    FROM MEMBERSHIPS M
             JOIN EXPENSES E
                  ON M.ID = E.MEMBERSHIP_ID
    WHERE M.GROUP_ID = G_ID
    GROUP BY M.USER_ID
    UNION
    SELECT -1, 0
    FROM DUAL
    ORDER BY TOTALSPENDING DESC
        FETCH FIRST 1 ROWS ONLY;

    RETURN V_USER_ID;
END;
/

BEGIN
    DBMS_OUTPUT.PUT_LINE(MOST_SPENDING_USER_IN_GROUP(5));
END;
/

CREATE OR REPLACE FUNCTION get_expense_price_in_another_curr(expense_id expenses.id%TYPE, curr_id currencies.id%TYPE)
    RETURN expenses.price%TYPE
AS
    v_price expenses.price%TYPE;
    v_src_er currencies.exchange_rate%TYPE;
    v_dest_er currencies.exchange_rate%TYPE;
BEGIN
    SELECT e.price, c.exchange_rate INTO v_price, v_src_er FROM expenses e
    JOIN currencies c ON (e.currency_id = c.id)
    WHERE e.id = expense_id;

    SELECT exchange_rate INTO v_dest_er FROM currencies WHERE id = curr_id;

    v_price := ( v_price * v_src_er) / v_dest_er;
    return v_price;
END;
/
