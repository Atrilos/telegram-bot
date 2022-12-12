-- liquibase formatted sql

-- changeset atrilos:1
CREATE TABLE notification_task
(
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    chat_id          VARCHAR(255),
    text             VARCHAR(255),
    notify_date_time TIMESTAMP WITHOUT TIME ZONE,
    deleted          BOOLEAN DEFAULT FALSE                   NOT NULL,
    CONSTRAINT pk_notification_task PRIMARY KEY (id)
);
-- rollback drop table notification_task;

-- changeset atrilos:2
CREATE INDEX notify_date_time_idx ON notification_task USING hash (notify_date_time);
-- rollback drop index notify_date_time_idx;