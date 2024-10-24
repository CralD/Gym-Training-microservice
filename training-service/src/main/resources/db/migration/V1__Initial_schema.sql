CREATE TABLE TRAINER (
    trainer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    is_active BOOLEAN
);

CREATE TABLE TRAINING_DETAILS (
    user_id BIGINT,
    year_month VARCHAR(255),
    summary_duration DOUBLE,
    PRIMARY KEY (user_id, year_month),
    FOREIGN KEY (user_id) REFERENCES TRAINER(trainer_id)
);