
create table qrcode (
    id uuid not null primary key,
    txid varchar not null unique,
    qrcode_value numeric not null,
    description varchar,
    status varchar,
    expiration_date date not null,
    due_date date,
    updated_at timestamp not null
);