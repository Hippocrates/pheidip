-- Some data to use with unit tests

INSERT INTO Donor VALUES(1, 'test1@test.com', 'smk', 'Stephen', 'Kiazyk');
INSERT INTO Donor VALUES(2, 'test2@test.com', 'analias', 'Stefan', 'Ksiazyk');
INSERT INTO Donor VALUES(3, 'test3@test.com', 'demonrushfan6969', 'Brooks', 'Cracktackle');
INSERT INTO Donor VALUES(4, 'test4@test.com', 'anotheralias', '', '');
INSERT INTO Donor VALUES(5, null, null, '', '');
INSERT INTO Donor VALUES(6, null, null, '', '');

INSERT INTO SpeedRun VALUES(1, 'run 1');
INSERT INTO SpeedRun VALUES(2, 'run 2');
INSERT INTO SpeedRun VALUES(3, 'yet another run');

INSERT INTO Choice VALUES(1, 1, 'naming something');
INSERT INTO Choice VALUES(2, 1, 'naming something else');
INSERT INTO Choice VALUES(3, 1, 'a path of some sort');
INSERT INTO Choice VALUES(4, 2, 'not part of game 1');

INSERT INTO ChoiceOption VALUES(1, 1, 'name 1');
INSERT INTO ChoiceOption VALUES(2, 1, 'name 2');
INSERT INTO ChoiceOption VALUES(3, 2, 'name 1');

INSERT INTO Challenge VALUES(1, 2, 'challenge 1', 20.00);
INSERT INTO Challenge VALUES(2, 1, 'challenge 2', 50.00);
INSERT INTO Challenge VALUES(3, 2, 'challenge 2', 100.00);
INSERT INTO Challenge VALUES(4, 2, 'challenge whatever', 150.00);

INSERT INTO Donation VALUES(1, 1, 'LOCAL', null, 'PENDING', 12.40, '2004-10-18 23:32:34', null);
INSERT INTO Donation VALUES(2, 2, 'LOCAL', null, 'PENDING', 25.00, '2006-10-18 00:32:34', null);
INSERT INTO Donation VALUES(3, 4, 'LOCAL', null, 'PENDING', 25.00, '2006-10-18 00:32:34', null);
INSERT INTO Donation VALUES(4, 4, 'LOCAL', null, 'PENDING',  5.00, '2006-10-18 12:44:55', null);
INSERT INTO Donation VALUES(5, 6, 'LOCAL', null, 'PENDING', 25.00, '2006-10-18 00:32:34', null);
INSERT INTO Donation VALUES(7, 3, 'CHIPIN', '1234567890', 'PENDING', 15.00, '2006-11-13 10:00:00', 'Some comment text.');

INSERT INTO ChoiceBid VALUES(1, 1, 1, 10.00);
INSERT INTO ChoiceBid VALUES(2, 1, 1, 5.00);

INSERT INTO ChallengeBid VALUES(1, 1, 1, 10.00);
INSERT INTO ChallengeBid VALUES(2, 1, 1, 10.00);
INSERT INTO ChallengeBid VALUES(3, 2, 1, 10.00);
