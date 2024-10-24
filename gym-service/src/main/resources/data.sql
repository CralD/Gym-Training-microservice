-- DML
INSERT INTO USERS (FIRSTNAME, LASTNAME, USERNAME, PASSWORD, IS_ACTIVE)
VALUES ('Raul', 'Perez', 'Raul.perez', 'as45ww', FALSE),
       ('Maria', 'Garcia', 'maria.garcia', 'qwerty123', TRUE),
       ('John', 'Smith', 'john.smith', 'password12', TRUE),
       ('Lisa', 'Johnson', 'lisa.johnson', 'hello123', TRUE),
       ('Michael', 'Brown', 'michael.brown', 'test123', TRUE),
       ('Sarah', 'Martinez', 'sarah.martinez', '123456', FALSE),
       ('David', 'Taylor', 'david.taylor', 'david123', TRUE),
       ('Emma', 'Anderson', 'emma.anderson', 'emma456', TRUE),
       ('James', 'Wilson', 'james.wilson', 'wilson789', FALSE),
       ('Olivia', 'Thomas', 'olivia.thomas', 'thomas987', TRUE);

INSERT INTO TRAINEE (DATEOFBIRTH, ADDRESS, USERID)
VALUES ('2001-05-15', 'CARRERA 33 CALLE 99', 1),
       ('1998-08-20', 'CARRERA 21 CALLE 56', 2),
       ('2000-02-10', 'CARRERA 45 CALLE 33', 3),
       ('1999-11-30', 'CARRERA 7 CALLE 9', 4),
       ('2002-04-05', 'CARRERA 17 CALLE 44', 5),
       ('1997-09-25', 'CARRERA 25 CALLE 77', 6);

       INSERT INTO TRAININGTYPE (TYPENAME)
       VALUES ('Cardio'),
              ('Strength'),
              ('Flexibility'),
              ('Mobility');

INSERT INTO TRAINER (SPECIALIZATION, USERID) VALUES (1,7);
INSERT INTO TRAINER (SPECIALIZATION, USERID) VALUES (2,8);
INSERT INTO TRAINER (SPECIALIZATION, USERID) VALUES (3,9);
INSERT INTO TRAINER (SPECIALIZATION, USERID) VALUES (4,10);



INSERT INTO TRAINING (TRAINEEID, TRAINERID, TRAININGNAME, TRAININGTYPEID, TRAININGDATE, TRAININGDURATION)
VALUES (1, 1, 'Cardio Training', 1, '2024-05-15', 30.0),
       (2, 2, 'Strength Training', 2, '2024-05-15', 40.0),
       (3, 3, 'Flexibility Training', 3, '2024-05-16', 60.0),
       (4, 4, 'Mobility Training', 4, '2024-05-24', 30.0),
       (5, 1, 'Cardio Training', 1, '2024-05-17', 25.0),
       (6, 3, 'Flexibility Training', 3, '2024-05-18', 40.0);

INSERT INTO TRAINEE_TRAINER(TRAINEE_ID, TRAINER_ID) VALUES (1,1),
(2,2),
(3,3),
(4,4),
(5,1),
(6,3);


COMMIT;
