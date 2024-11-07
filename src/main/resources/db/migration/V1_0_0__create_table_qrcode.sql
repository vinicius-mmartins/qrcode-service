
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

comment on column qrcode.id is 'Unique identifier';
comment on column qrcode.txid is 'QRCode unique identifier passed by the client for idempotency';
comment on column qrcode.qrcode_value is 'QRCode value';
comment on column qrcode.description is 'QRCode description';
comment on column qrcode.status is 'QRCode status';
comment on column qrcode.expiration_date is 'QRCode expiration date';
comment on column qrcode.due_date is 'QRCode due date. This field differentiates QRCode with and without due date';
comment on column qrcode.created_at is 'Creation date';
comment on column qrcode.updated_at is 'Last update date';