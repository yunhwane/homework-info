drop table if exists my_database.members;

create table my_database.members (
    id bigint auto_increment primary key,
    username        varchar(255) not null,
    view_count bigint default 0 not null,
    registered_at timestamp default CURRENT_TIMESTAMP null,
    created_at  timestamp default CURRENT_TIMESTAMP null,
    updated_at  timestamp null on update CURRENT_TIMESTAMP
);

-- 1. USERNAME 정렬용 covering index
CREATE INDEX idx_members_name_covering
    ON my_database.members (username, id, view_count, registered_at);

-- 2. REGISTERED_AT 정렬용 covering index
CREATE INDEX idx_members_registered_covering
    ON my_database.members (registered_at, id, username, view_count);
