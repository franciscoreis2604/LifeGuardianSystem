CREATE TABLE Roles(
    rId SERIAL PRIMARY KEY,
    rName VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Users (
    uId UUID NOT NULL PRIMARY KEY,
    uName VARCHAR(100) NOT NULL,
    uPhone VARCHAR(9) NOT NULL UNIQUE,
    uBirthday DATE NOT NULL,
    uRole INTEGER NOT NULL,

    FOREIGN KEY (uRole) REFERENCES Roles (rId)
);

CREATE TABLE Login (
    lId UUID NOT NULL,
    lUsername VARCHAR(100) NOT NULL,
    lPassword VARCHAR(100) NOT NULL,

    FOREIGN KEY (lId) REFERENCES Users(uId),
    CONSTRAINT PK_Login PRIMARY KEY (lUsername)
);

CREATE TABLE Sensors(
    sId SMALLSERIAL NOT NULL PRIMARY KEY,
    sName VARCHAR(100) NOT NULL
);

CREATE TABLE SensorDataUnprocessed(
    pId UUID NOT NULL,
    uId UUID NOT NULL,
    sId SMALLINT NOT NULL,
    sValue DECIMAL(5,2) NOT NULL,
    sTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_SensorDataUnprocessed PRIMARY KEY (uId,pId,sId),
        FOREIGN KEY (uId) REFERENCES Users(uId),
        FOREIGN KEY (sId) REFERENCES Sensors(sId)
    );
);

CREATE TABLE SensorData(
    pId UUID NOT NULL,
    uId UUID NOT NULL,
    sId SMALLINT NOT NULL,
    sValue DECIMAL(5,2) NOT NULL,
    sTimestamp TIMESTAMP NOT NULL,
    sRisk INT NOT NULL,
    sRiskDescription VARCHAR(100) NOT NULL,
    sProcessedDelayedHigh BOOLEAN NOT NULL,
    sProcessedDelayedMedium BOOLEAN NOT NULL,
    sProcessedDelayedLow BOOLEAN NOT NULL,

    CONSTRAINT PK_SensorData PRIMARY KEY (uId,pId,sId),
    FOREIGN KEY (uId) REFERENCES Users(uId),
    FOREIGN KEY (sId) REFERENCES Sensors(sId)
);

CREATE TABLE HealthParams(
    sId SMALLINT NOT NULL,
    uId UUID NOT NULL,
    hMinVal DECIMAL(5,2) NOT NULL,
    hMaxVal DECIMAL(5,2) NOT NULL,

    FOREIGN KEY (sId) REFERENCES Sensors(sId),
    FOREIGN KEY (uId) REFERENCES Users(uId),
    CONSTRAINT PK_HealthParams PRIMARY KEY (sId,uId)
);

CREATE TABLE ContactType(
    cType VARCHAR(50) PRIMARY KEY NOT NULL
);

CREATE TABLE SOSContacts(
    uId UUID NOT NULL,
    cName VARCHAR(100) NOT NULL,
    cType VARCHAR(50) NOT NULL,
    cValue VARCHAR(50) NOT NULL,

    FOREIGN KEY (uId) REFERENCES Users(uId),
    FOREIGN KEY (cType) REFERENCES ContactType(cType),

    CONSTRAINT PK_SOSContacts PRIMARY KEY(uId,cName,cType),
    CONSTRAINT check_min_length check (length(cValue) >= 5)
);