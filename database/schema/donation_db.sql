-- SQL Table definitions for the donation database

-- This was generated automatically by hibernate, so it'll look pretty bad

create table Donor 
(
  ID integer not null, 
  EMAIL varchar(255),
  FIRSTNAME varchar(255), 
  LASTNAME varchar(255),
  ALIAS varchar(255),
  
  CONSTRAINT DonorEmailUnique UNIQUE (EMAIL),
  CONSTRAINT DonorEmailLowerCase CHECK (EMAIL = lower(EMAIL)),
  CONSTRAINT DonorAliasUnique UNIQUE (ALIAS),
  CONSTRAINT DonorAliasLowerCase CHECK (ALIAS = lower(ALIAS)),
  
  CONSTRAINT DonorPK PRIMARY KEY (ID)
);

create table Donation 
(
  ID integer not null, 
  TIMERECEIVED datetime, 
  AMOUNT decimal(19,2), 
  COMMENT longtext, 
  DOMAIN varchar(16), 
  DOMAINID varchar(64),
  BIDSTATE varchar(64),
  READSTATE varchar(64),
  COMMENTSTATE varchar(64),
  DONOR integer, 
  
  CONSTRAINT DonationFKDonor FOREIGN KEY (DONOR) REFERENCES Donor(ID),
  CONSTRAINT DonationDomainIdUnique UNIQUE (DOMAIN, DOMAINID),
  CONSTRAINT DonationAmountValid CHECK (AMOUNT > 0 OR AMOUNT = null),
  
  CONSTRAINT DonationPK PRIMARY KEY (ID)
);

create table SpeedRun 
(
  ID integer not null, 
  NAME varchar(255),
  RUNNERS varchar(255),
  DESCRIPTION varchar(255),
  STARTTIME datetime,
  ENDTIME datetime,
  SORTKEY integer, 

  CONSTRAINT SpeedRunNameUnique UNIQUE (NAME),
  CONSTRAINT SpeedRunNameLowerCase CHECK (NAME = lower(NAME)),
  -- CONSTRAINT SpeedRunSortKeyUnique UNIQUE(SORTKEY),
  
  CONSTRAINT SpeedRunPK PRIMARY KEY (ID)
);

create table Challenge 
(
  ID integer not null, 
  NAME varchar(255),
  DESCRIPTION varchar(255),
  BIDSTATE varchar(255),
  SPEEDRUN integer,
  GOALAMOUNT decimal(19,2),

  CONSTRAINT ChallengeFKSpeedRun FOREIGN KEY (SPEEDRUN) REFERENCES SpeedRun(ID),
  CONSTRAINT ChallengeNameLowerCase CHECK (NAME = lower(NAME)),
  CONSTRAINT ChallengeNameUnique UNIQUE (NAME, SPEEDRUN),
  CONSTRAINT ChallengeAmountValid CHECK(goalAmount >= 0),

  CONSTRAINT CballengePK PRIMARY KEY (ID)
);

create table ChallengeBid 
(
  ID integer not null,
  AMOUNT decimal(19,2),
  DONATION integer, 
  CHALLENGE integer,
  
  CONSTRAINT ChallengeBidFKDonation FOREIGN KEY (DONATION) REFERENCES Donation(ID),
  CONSTRAINT ChallengeBidFKChallenge FOREIGN KEY (CHALLENGE) REFERENCES Challenge(ID),
  CONSTRAINT ChallengeBidAmountValid CHECK (AMOUNT > 0),
  
  CONSTRAINT ChallengeBidPK PRIMARY KEY (ID)
);
  
create table Choice 
(
  ID integer not null, 
  NAME varchar(255),
  DESCRIPTION varchar(255),
  BIDSTATE varchar(255),
  SPEEDRUN integer, 

  CONSTRAINT ChoiceFKSpeedRun FOREIGN KEY (SPEEDRUN) REFERENCES SpeedRun(ID),
  CONSTRAINT ChoiceNameUnique UNIQUE (NAME, SPEEDRUN),
  CONSTRAINT ChoiceNameLowerCase CHECK (NAME = lower(NAME)),
  
  CONSTRAINT ChoicePK PRIMARY KEY (ID)
);

create table ChoiceOption
(
  ID integer not null,
  NAME varchar(255), 
  CHOICE integer,
  
  CONSTRAINT OptionFKChoice FOREIGN KEY (CHOICE) REFERENCES Choice(ID),
  CONSTRAINT OptionNameUnique UNIQUE(CHOICE, NAME),
  CONSTRAINT OptionNameLowerCase CHECK (NAME = lower(NAME)),
  
  CONSTRAINT OptionPK PRIMARY KEY (ID)
);

create table ChoiceBid 
(
  ID integer not null, 
  AMOUNT decimal(19,2), 
  DONATION integer,
  CHOICEOPTION integer,
  
  CONSTRAINT ChoiceBidFKDonation FOREIGN KEY (DONATION) REFERENCES Donation(ID),
  CONSTRAINT ChoiceBidFKOption FOREIGN KEY (CHOICEOPTION) REFERENCES ChoiceOption(ID),
  CONSTRAINT ChoiceBidAmountValid CHECK (AMOUNT > 0),

  CONSTRAINT ChoiceBidPK PRIMARY KEY (ID)
);

create table Prize
(
  ID integer not null, 
  NAME varchar(255), 
  IMAGEURL varchar(255), 
  SORTKEY integer, 
  DESCRIPTION varchar(255), 
  WINNER integer, 
  STARTGAME integer,
  ENDGAME integer,
  DRAWMETHOD varchar(255) not null, 
  MINIMUMBID decimal(19,2) not null,
  
  CONSTRAINT PrizeNameUnique UNIQUE(NAME),
  CONSTRAINT PrizeNameLowerCase CHECK(NAME = lower(NAME)),
  CONSTRAINT PrizeFKDonor FOREIGN KEY (WINNER) REFERENCES Donor (ID),
  CONSTRAINT PrizeFKSpeedRunStart FOREIGN KEY (STARTGAME) REFERENCES SPEEDRUN(ID),
  CONSTRAINT PrizeFKSpeedRunEnd FOREIGN KEY (ENDGAME) REFERENCES SPEEDRUN(ID),
  
  CONSTRAINT PrizePK PRIMARY KEY(ID)
);
