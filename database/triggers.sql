/*
Trigger to archive deleted groups
 */
CREATE OR REPLACE TRIGGER archive_group
BEFORE DELETE ON groups
FOR EACH ROW
BEGIN
INSERT INTO archived_groups (id, name)
VALUES (:old.id, :old.name);

INSERT INTO archived_memberships (id, group_id, user_id, role_id)
SELECT base_membership_seq.NEXTVAL, m.group_id, m.user_id, m.role_id
FROM memberships m
WHERE m.group_id = :old.id;

DELETE FROM memberships WHERE group_id = :old.id;
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
