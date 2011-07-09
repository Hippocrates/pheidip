-- SQL Table definitions for the donation database

-- MySQL does not allow inline constraints, so I must put them seperately
-- Also, there is no general support for ignorecase fields in sql, so that
-- must be handled externally

CREATE TABLE Donor
(
  donorId INTEGER,
  email VARCHAR(128),
  alias VARCHAR(31),
  firstName VARCHAR(31),
  lastName VARCHAR(31),

  CONSTRAINT DonorEmailUnique UNIQUE (email),
  CONSTRAINT DonorEmailLowerCase CHECK (email = lower(email)),
  CONSTRAINT DonorAliasUnique UNIQUE (alias),
  CONSTRAINT DonorAliasLowerCase CHECK (alias = lower(alias)),
  
  CONSTRAINT DonorPK PRIMARY KEY (donorId)
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

CREATE TABLE DonationReadState
(
  donationReadStateId VARCHAR(16),
  PRIMARY KEY (donationReadStateId)
);

INSERT INTO DonationReadState VALUES ('PENDING');
INSERT INTO DonationReadState VALUES ('AMOUNT_READ');
INSERT INTO DonationReadState VALUES ('COMMENT_READ');
INSERT INTO DonationReadState VALUES ('FLAGGED');

CREATE TABLE Donation
(
  donationId INTEGER,
  donorId INTEGER,
  domain VARCHAR(16),
  domainId VARCHAR(160),
  
  bidState VARCHAR(16),
  readState VARCHAR(16),
  
  amount DECIMAL(19,2),
  timeReceived DATETIME,
  comment VARCHAR(4096),

  CONSTRAINT DonationFKDonor FOREIGN KEY (donorId) REFERENCES Donor(donorId),
  CONSTRAINT DonationFKDomain FOREIGN KEY (domain) REFERENCES DonationDomain (donationDomainId),
  CONSTRAINT DonationDomainIdUnique UNIQUE (domain, domainId),
  CONSTRAINT DonationFKBidState FOREIGN KEY (bidState) REFERENCES DonationBidState (donationBidStateId),
  CONSTRAINT DonationFKReadState FOREIGN KEY (readState) REFERENCES DonationReadState (donationReadStateId),
  CONSTRAINT DonationAmountValid CHECK (amount > 0 OR amount = null),
  
  CONSTRAINT DonationPK PRIMARY KEY (donationId)
);

CREATE TABLE SpeedRun
(
  speedRunId INTEGER,
  name VARCHAR(63),
  description VARCHAR(1024),

  CONSTRAINT SpeedRunNameUnique UNIQUE (name),
  CONSTRAINT SpeedRunNameLowerCase CHECK (name = lower(name)),
  
  CONSTRAINT SpeedRunPK PRIMARY KEY (speedRunId)
);

CREATE TABLE Choice
(
  choiceId INTEGER,
  speedRunId INTEGER,
  name VARCHAR(63),
  description VARCHAR(1024),

  CONSTRAINT ChoiceFKSpeedRun FOREIGN KEY (speedRunId) REFERENCES SpeedRun(speedRunId),
  CONSTRAINT ChoiceNameUnique UNIQUE (speedRunId, name),
  CONSTRAINT ChoiceNameLowerCase CHECK (name = lower(name)),
  
  CONSTRAINT ChoicePK PRIMARY KEY (choiceId)
);

CREATE TABLE ChoiceOption
(
  optionId INTEGER,
  choiceId INTEGER,
  name VARCHAR(63),
  
  CONSTRAINT OptionFKChoice FOREIGN KEY (choiceId) REFERENCES Choice(choiceId),
  CONSTRAINT OptionNameUnique UNIQUE(choiceId, name),
  CONSTRAINT OptionNameLowerCase CHECK (name = lower(name)),
  
  CONSTRAINT OptionPK PRIMARY KEY (optionId)
);

CREATE TABLE Challenge
(
  challengeId INTEGER,
  speedRunId INTEGER,
  name VARCHAR(63),
  goalAmount DECIMAL(19,2),
  description VARCHAR(1024),
  
  CONSTRAINT ChallengeFKSpeedRun FOREIGN KEY (speedRunId) REFERENCES SpeedRun(speedRunId),
  CONSTRAINT ChallengeNameUnique UNIQUE (speedRunId, name),
  CONSTRAINT ChallengeNameLowerCase CHECK (name = lower(name)),
  CONSTRAINT ChallengeAmountValid CHECK(goalAmount >= 0),
  
  CONSTRAINT ChallengePK PRIMARY KEY (challengeId)
);

CREATE TABLE ChoiceBid
(
  choiceBidId INTEGER,
  optionId INTEGER,
  donationId INTEGER,
  amount DECIMAL(19,2),

  CONSTRAINT ChoiceBidFKDonation FOREIGN KEY (donationId) REFERENCES Donation(donationId),
  CONSTRAINT ChoiceBidFKOption FOREIGN KEY (optionId) REFERENCES ChoiceOption(optionId),
  CONSTRAINT ChoiceBidAmountValid CHECK (amount > 0),

  CONSTRAINT ChoiceBidPK PRIMARY KEY (choiceBidId)
);

CREATE TABLE ChallengeBid
(
  challengeBidId INTEGER,
  challengeId INTEGER,
  donationId INTEGER,
  amount DECIMAL(19,2),

  CONSTRAINT ChallengeBidFKDonation FOREIGN KEY (donationId) REFERENCES Donation(donationId),
  CONSTRAINT ChallengeBidFKChallenge FOREIGN KEY (challengeId) REFERENCES Challenge(challengeId),
  CONSTRAINT ChallengeBidAmountValid CHECK (amount > 0),
  
  CONSTRAINT ChallengeBidPK PRIMARY KEY (challengeBidId)
);

CREATE TABLE Prize
(
  prizeId INTEGER,
  name VARCHAR(63),
  imageURL VARCHAR(1024),
  description VARCHAR(1024),
  
  CONSTRAINT PrizeNameUnique UNIQUE(name),
  CONSTRAINT PrizeNameLowerCase CHECK(name = lower(name)),
  
  CONSTRAINT PrizePK PRIMARY KEY(prizeId)
);

CREATE TABLE PrizeWinner
(
  prizeId INTEGER,
  donorId INTEGER,
  
  CONSTRAINT PrizeWinnerFKDonor FOREIGN KEY (donorId) REFERENCES Donor (donorId),
  
  CONSTRAINT PrizeWinnerPK PRIMARY KEY(prizeId)
);