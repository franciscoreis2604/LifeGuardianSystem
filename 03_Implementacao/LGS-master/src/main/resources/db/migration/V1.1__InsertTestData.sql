
-- Todos os roles tem de ser "ROLE_" --
INSERT INTO Roles(rName) VALUES('ROLE_ADMIN');
INSERT INTO Roles(rName) VALUES('ROLE_USER');

-- USERS --
INSERT INTO USERS VALUES('62f2137e-7bce-46f6-8c18-30ed24434c2f','John Doe','123456789','1970-02-14',2);
INSERT INTO USERS VALUES('5f82e7b9-d3a9-45df-8287-bacd7da159e3','Jorge Floyd','987654321','1974-05-25',1);

-- psswrd: nkisbestk --
INSERT INTO LOGIN VALUES('62f2137e-7bce-46f6-8c18-30ed24434c2f','john@doe.org','$2a$10$fSpLazVz7H6gDWBCfUBUoulFBVn1s5vkBLtuUYWOv425b0t0v2o/i');

-- psswrd: blm --
INSERT INTO LOGIN VALUES('5f82e7b9-d3a9-45df-8287-bacd7da159e3','jorge@floyd.org','$2y$10$8ybq.AqziAkTkB4V8pses.wExK9eD9.7v5NNb7tuxs7Z.QrwC7aZ6');

-- SENSORS --
INSERT INTO Sensors(sName) VALUES('Temperature');
INSERT INTO Sensors(sName) VALUES('Pulse');


-- Health Parameters--
-- INSERT INTO HealthParams(sId,uid,hMinVal,hMaxVal) VALUES(1,'62f2137e-7bce-46f6-8c18-30ed24434c2f', 36.1, 37.2),
                                                  --  (2, '62f2137e-7bce-46f6-8c18-30ed24434c2f', 0, 1),
                                                  --  (3, '62f2137e-7bce-46f6-8c18-30ed24434c2f' ,60, 100);

-- Contact Type --
INSERT INTO ContactType VALUES('Email'),('Telephone'),('Mobile');


