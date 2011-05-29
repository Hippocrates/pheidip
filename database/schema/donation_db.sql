-- SQL Table definitions for the donation database

-- MySQL does not allow inline constraints, so I must put them seperately
-- Also, there is no general support for ignorecase fields in sql, so that
-- must be handled externally

CREATE TABLE Donor
(
  donorId INTEGER,
  email VARCHAR(255),
  alias VARCHAR(31),
  firstName VARCHAR(31),
  lastName VARCHAR(31),

  UNIQUE (email),
  CHECK (email = lower(email)),
  UNIQUE (alias),
  CHECK (alias = lower(alias)),
  
  PRIMARY KEY (donorId)
);

CREATE TABLE DonationDomain
(
  donationDomainId VARCHAR (16),
  PRIMARY KEY (donationDomainId)
);

INSERT INTO DonationDomain VALUES('LOCAL');
INSERT INTO DonationDomain VALUES('CHIPIN');

CREATE TABLE DonationBidState
(
  donationBidStateId VARCHAR(16),
  PRIMARY KEY (donationBidStateId)
);

INSERT INTO DonationBidState VALUES ('PENDING');
INSERT INTO DonationBidState VALUES ('PROCESSED');
INSERT INTO DonationBidState VALUES ('FLAGGED');

CREATE TABLE Donation
(
  donationId INTEGER,
  donorId INTEGER,
  domain VARCHAR(16),
  domainId VARCHAR(64),
  
  bidState VARCHAR(16),
  
  amount DECIMAL(19,2),
  timeReceived DATETIME,
  comment VARCHAR(1024),

  FOREIGN KEY (donorId) REFERENCES Donor(donorId),
  FOREIGN KEY (domain) REFERENCES DonationDomain (donationDomainId),
  UNIQUE (domain, domainId),
  FOREIGN KEY (bidState) REFERENCES DonationBidState (donationBidStateId),
  CHECK (amount > 0 OR amount = null),
  
  PRIMARY KEY (donationId)
);

CREATE TABLE SpeedRun
(
  speedRunId INTEGER,
  name VARCHAR(63),

  UNIQUE (name),
  CHECK (name = lower(name)),
  
  PRIMARY KEY (speedRunId)
);

CREATE TABLE Choice
(
  choiceId INTEGER,
  speedRunId INTEGER,
  name VARCHAR(63),

  FOREIGN KEY (speedRunId) REFERENCES SpeedRun(speedRunId),
  UNIQUE (name),
  CHECK (name = lower(name)),
  
  PRIMARY KEY (choiceId)
);

CREATE TABLE ChoiceOption
(
  optionId INTEGER,
  choiceId INTEGER,
  name VARCHAR(63),
  
  FOREIGN KEY (choiceId) REFERENCES Choice(choiceId),
  UNIQUE(choiceId, name),
  CHECK (name = lower(name)),
  
  PRIMARY KEY (optionId)
);

CREATE TABLE Challenge
(
  challengeId INTEGER,
  speedRunId INTEGER,
  name VARCHAR(63),
  goalAmount DECIMAL(19,2),
  
  FOREIGN KEY (speedRunId) REFERENCES SpeedRun(speedRunId),
  UNIQUE (speedRunId, name),
  CHECK (name = lower(name)),
  CHECK(goalAmount > 0),
  
  PRIMARY KEY (challengeId)
);

CREATE TABLE ChoiceBid
(
  choiceBidId INTEGER,
  optionId INTEGER,
  donationId INTEGER,
  amount DECIMAL(19,2),

  FOREIGN KEY (donationId) REFERENCES Donation(donationId),
  FOREIGN KEY (optionId) REFERENCES ChoiceOption(optionId),
  CHECK (amount > 0),

  PRIMARY KEY (choiceBidId)
);

CREATE TABLE ChallengeBid
(
  challengeBidId INTEGER,
  challengeId INTEGER,
  donationId INTEGER,
  amount DECIMAL(19,2),

  FOREIGN KEY (donationId) REFERENCES Donation(donationId),
  FOREIGN KEY (challengeId) REFERENCES Challenge(challengeId),
  CHECK (amount > 0),
  
  PRIMARY KEY (challengeBidId)
);
