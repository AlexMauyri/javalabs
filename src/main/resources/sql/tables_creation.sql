CREATE TABLE calculation_result
(
    calculationID SERIAL PRIMARY KEY,
    appliedValue  DOUBLE PRECISION NOT NULL,
    resultValue   DOUBLE PRECISION NOT NULL
);

CREATE TABLE applied_functions_data
(
    applicationID SERIAL PRIMARY KEY,
    calculationID INTEGER REFERENCES calculation_result (calculationID) ON DELETE CASCADE NOT NULL,
    functionOrder INTEGER                                                                 NOT NULL,
    functionHash  BIGINT                                                                  NOT NULL
);

CREATE INDEX idx_calculation_result_appliedValue ON calculation_result (appliedValue);
CREATE INDEX idx_applied_functions_data_calculationID ON applied_functions_data (calculationID);
CREATE INDEX idx_applied_functions_data_functionOrder ON applied_functions_data (functionOrder);