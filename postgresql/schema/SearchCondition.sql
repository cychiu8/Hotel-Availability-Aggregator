CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE IF NOT EXISTS search_condition (
    id UUID DEFAULT uuid_generate_v4 (),
    dest VARCHAR(255),
    checkin DATE,
    checkout DATE,
    group_adults INTEGER,
    group_children INTEGER,
    no_rooms INTEGER,
    PRIMARY KEY (id)
);