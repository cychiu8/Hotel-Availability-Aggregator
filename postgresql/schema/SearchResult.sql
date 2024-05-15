CREATE TABLE IF NOT EXISTS search_result (
    id UUID DEFAULT uuid_generate_v4 (),
    execute_time TIMESTAMP,
    response_time BIGINT,
    number_of_results INT,
    min_price INT,
    max_price INT,
    execute_url TEXT,
    search_condition_id UUID,
    PRIMARY KEY (id),
    FOREIGN KEY (search_condition_id) REFERENCES search_condition(id)
);