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

CREATE INDEX IF NOT EXISTS idx_calculation_calculation_hash ON calculation (calculation_hash);
CREATE INDEX IF NOT EXISTS idx_applied_function_calculation_id ON calculation (calculation_id);
CREATE INDEX IF NOT EXISTS idx_applied_function_function_order ON applied_function (function_order);

CREATE TABLE IF NOT EXISTS log
(
    id        SERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL,
    message   TEXT      NOT NULL
);

CREATE TABLE IF NOT EXISTS roles
(
    role_id   SERIAL PRIMARY KEY,
    role_name TEXT
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  SERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(30) NOT NULL,
    role_id  INT         NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles (role_id)
);