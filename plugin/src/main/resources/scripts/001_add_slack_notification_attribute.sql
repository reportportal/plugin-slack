DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM attribute WHERE name = 'notifications.slack.enabled') THEN
        WITH new_attr AS (
            INSERT INTO attribute (name)
            VALUES ('notifications.slack.enabled')
            RETURNING id
       ),
       existing_projects as (
            SELECT DISTINCT project_id FROM project_attribute
        )
        INSERT INTO project_attribute (attribute_id, value, project_id)
        SELECT new_attr.id, 'true', project_id
        FROM existing_projects, new_attr;
    END IF;
END
$$;