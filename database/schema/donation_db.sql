-- SQL Table definitions for the donation database

-- This was generated automatically by hibernate, so it'll look pretty bad

create table Challenge (ID integer not null, NAME varchar(255) check (NAME = lower(NAME)), DESCRIPTION varchar(255), BIDSTATE varchar(255), SPEEDRUN integer, GOALAMOUNT decimal(19,2), primary key (ID), unique (NAME, SPEEDRUN));
create table ChallengeBid (ID integer not null, AMOUNT decimal(19,2), DONATION integer, CHALLENGE integer, primary key (ID));
create table Choice (ID integer not null, NAME varchar(255) check (NAME = lower(NAME)), DESCRIPTION varchar(255), BIDSTATE varchar(255), SPEEDRUN integer, primary key (ID), unique (NAME, SPEEDRUN));
create table ChoiceBid (ID integer not null, AMOUNT decimal(19,2), DONATION integer, CHOICEOPTION integer, primary key (ID));
create table ChoiceOption (ID integer not null, NAME varchar(255) check (NAME = lower(NAME)), CHOICE integer, primary key (ID), unique (NAME, CHOICE));
create table Donation (ID integer not null, TIMERECEIVED datetime, AMOUNT decimal(19,2), COMMENT longtext, DOMAIN varchar(255), DOMAINID varchar(255), BIDSTATE varchar(255), READSTATE varchar(255), COMMENTSTATE varchar(255), DONOR integer, primary key (ID), unique (DOMAIN, DOMAINID));
create table Donor (ID integer not null, EMAIL varchar(255) unique check (EMAIL = lower(EMAIL)), FIRSTNAME varchar(255), LASTNAME varchar(255), ALIAS varchar(255) unique check (ALIAS = lower(ALIAS)), primary key (ID));
create table Prize (ID integer not null, NAME varchar(255) unique check (NAME = lower(NAME)), IMAGEURL varchar(255), sortKey integer unique, DESCRIPTION varchar(255), WINNER integer, primary key (ID));
create table SpeedRun (ID integer not null, NAME varchar(255) unique check (NAME = lower(NAME)), DESCRIPTION varchar(255), sortKey integer unique, primary key (ID));
alter table Challenge add index BidFKSpeedRuncb0c9c43 (SPEEDRUN), add constraint BidFKSpeedRuncb0c9c43 foreign key (SPEEDRUN) references SpeedRun (ID);
alter table ChallengeBid add index DonationBidFKDonation107d5dba (DONATION), add constraint DonationBidFKDonation107d5dba foreign key (DONATION) references Donation (ID);
alter table ChallengeBid add index ChallengeBidFKChallenge (CHALLENGE), add constraint ChallengeBidFKChallenge foreign key (CHALLENGE) references Challenge (ID);
alter table Choice add index BidFKSpeedRun784249c1 (SPEEDRUN), add constraint BidFKSpeedRun784249c1 foreign key (SPEEDRUN) references SpeedRun (ID);
alter table ChoiceBid add index DonationBidFKDonationaa05d77c (DONATION), add constraint DonationBidFKDonationaa05d77c foreign key (DONATION) references Donation (ID);
alter table ChoiceBid add index ChoiceBidFKOption (CHOICEOPTION), add constraint ChoiceBidFKOption foreign key (CHOICEOPTION) references ChoiceOption (ID);
alter table ChoiceOption add index ChoiceOptionFKChoice (CHOICE), add constraint ChoiceOptionFKChoice foreign key (CHOICE) references Choice (ID);
alter table Donation add index DonationFKDonor (DONOR), add constraint DonationFKDonor foreign key (DONOR) references Donor (ID);
alter table Prize add index PrizeFKDonor (WINNER), add constraint PrizeFKDonor foreign key (WINNER) references Donor (ID);
