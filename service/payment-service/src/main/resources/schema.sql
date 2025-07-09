DROP TABLE IF EXISTS point_events;
DROP TABLE IF EXISTS payment_histories;
DROP TABLE IF EXISTS payment_events;

create table my_database.payment_events
(
    id             bigint auto_increment primary key,
    buyer_id       bigint not null,
    order_id       varchar(255) not null,
    order_name     varchar(255) not null,
    payment_key    varchar(255) null,
    payment_type   enum ('NORMAL') null,
    payment_status enum ('NOT_STARTED','EXECUTING', 'STARTED', 'SUCCESS', 'FAILURE', 'UNKNOWN') default 'NOT_STARTED' null,
    amount         decimal(12, 2) not null,
    approved_at    timestamp null,
    payment_method enum ('EASY_PAY') null,
    psp_raw_data   json null,
    failed_count   bigint default 0 not null,
    retryable_count bigint default 0 not null,
    created_at     timestamp default CURRENT_TIMESTAMP null,
    updated_at     timestamp null on update CURRENT_TIMESTAMP
);


create table my_database.payment_histories
(
    id             bigint auto_increment primary key,
    payment_order_id bigint not null,
    previous_status enum ('NOT_STARTED','EXECUTING', 'STARTED', 'SUCCESS', 'FAILURE', 'UNKNOWN') default 'NOT_STARTED' null,
    new_status enum ('NOT_STARTED','EXECUTING', 'STARTED', 'SUCCESS', 'FAILURE', 'UNKNOWN') default 'NOT_STARTED' null,
    reason         varchar(255) null,
    created_at     timestamp default CURRENT_TIMESTAMP null,
    updated_at     timestamp null on update CURRENT_TIMESTAMP
);


create table my_database.point_events
(
    id             bigint auto_increment primary key,
    buyer_id       bigint not null,
    order_id       varchar(255) not null,
    points        decimal(12, 2) not null,
    point_type     enum ('REWARD', 'USE') null
);
