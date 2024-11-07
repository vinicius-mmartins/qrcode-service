
create table if not exists qrcode (
    id uuid not null primary key,
    txid varchar not null unique,
    qrcode_value numeric not null,
    description varchar,
    status varchar default 'OPEN',
    expiration_date date not null,
    due_date date,
    created_at timestamp not null,
    updated_at timestamp not null
);