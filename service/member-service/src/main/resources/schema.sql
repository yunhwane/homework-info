drop table if exists my_database.members;

create table my_database.members (
    id bigint auto_increment primary key,
    username        varchar(255) not null,
    view_count bigint default 0 not null,
    registered_at timestamp default CURRENT_TIMESTAMP null,
    created_at  timestamp default CURRENT_TIMESTAMP null,
    updated_at  timestamp null on update CURRENT_TIMESTAMP
);