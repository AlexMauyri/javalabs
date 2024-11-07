CREATE TABLE IF NOT EXISTS calculation
(
    calculation_id   SERIAL PRIMARY KEY,
    applied_value    DOUBLE PRECISION NOT NULL,
    result_value     DOUBLE PRECISION NOT NULL,
    calculation_hash BIGINT           NOT NULL
);

CREATE TABLE IF NOT EXISTS applied_function
(
    application_id      SERIAL PRIMARY KEY,
    calculation_id      INTEGER REFERENCES calculation (calculation_id) ON DELETE CASCADE NOT NULL,
    function_order      INTEGER                                                           NOT NULL,
    function_serialized BYTEA                                                             NOT NULL,
    mod_unmodifiable    BOOLEAN,
    mod_strict          BOOLEAN
);

CREATE INDEX idx_calculation_calculation_hash ON calculation (calculation_hash);
CREATE INDEX idx_applied_function_calculation_id ON calculation (calculation_id);
CREATE INDEX idx_applied_function_function_order ON applied_function (function_order);

CREATE TABLE IF NOT EXISTS log
(
    id        SERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    message   TEXT NOT NULL
);