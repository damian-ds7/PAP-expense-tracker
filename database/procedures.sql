CREATE OR REPLACE PROCEDURE remove_expired_tokens
AS
BEGIN
DELETE FROM refresh_tokens
WHERE expiry_date < SYSTIMESTAMP;
END;
/

BEGIN
    dbms_scheduler.create_job (
        job_name => 'remove_expired_tokens',
        job_type => 'PLSQL_BLOCK',
        job_action => 'BEGIN remove_expired_tokens; END;',
        start_date => TRUNC(SYSDATE) + 1,
        repeat_interval => 'FREQ=DAILY; BYHOUR=0; BYMINUTE=0; BYSECOND=0',
        enabled => TRUE,
        comments => 'Job to remove expired tokens every day at midnight'
    );
END;
/


CREATE OR REPLACE PROCEDURE calculate_group_expenses(curr_id currencies.id%TYPE)
AS
    CURSOR cr IS
        SELECT g.name, ROUND(SUM(get_expense_price_in_another_curr(e.id, curr_id)), 2) FROM expenses e
        JOIN memberships m ON (e.membership_id = m.id)
        JOIN groups g ON (m.group_id = g.id)
        GROUP BY g.id, g.name
        ORDER BY SUM(e.price) DESC;
    v_name groups.name%TYPE;
    v_sum expenses.price%TYPE;
    v_code currencies.symbol%TYPE;
BEGIN
    SELECT symbol INTO v_code FROM currencies WHERE curr_id = id;
    OPEN cr;
    LOOP
        FETCH cr into v_name, v_sum;
        EXIT WHEN cr%NOTFOUND;
        dbms_output.put_line('Group named ' || v_name || ' has total expenses equal to ' || v_sum || ' ' || v_code);
    END LOOP;
    CLOSE cr;
END;
/
