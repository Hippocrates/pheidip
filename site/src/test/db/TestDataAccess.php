<?php

require_once("simpletest/autorun.php");
require_once("test/db/DonationTestDB.php");
require_once("db/DataAccess.php");
require_once("test/db/DBTestConfig.php");

class TestDataAccess extends UnitTestCase
{
  private $db;
  private $data;
  
  function setUp()
  {
    $this->db = createDonationTestDB();
    
    $insertions = file_get_contents(getDonationTestDataDirectory() . "donation_bid_test_data_1.sql");
    
    $this->db->exec($insertions);
    
    $this->data = new DataAccess($this->db);
  }
  
  function tearDown()
  {
    $this->db = null;
  }
  
  function testGetAllSpeedRuns()
  {
    $games = $this->data->getAllSpeedRuns();
    
    $this->assertEqual(2, count($games));
  }
  
  function testGetSpeedRun()
  {
    $game = $this->data->getSpeedRun(1);
    
    $this->assertEqual("run 1", $game['name']);
    
    $game = $this->data->getSpeedRun(2);
    
    $this->assertEqual("run 2", $game['name']);
  }
  
  function testGetAllGameChoices()
  {
    $choices = $this->data->getSpeedRunChoices(1);
    
    $this->assertEqual(3, count($choices));
  }
  
  function testGetAllChoiceOptions()
  {
    $options = $this->data->getChoiceOptions(1);
    
    $this->assertEqual(2, count($options));
    
    $options2 = $this->data->getChoiceOptions(2);
    
    $this->assertEqual(1, count($options2));
  }

  function testGetAllGameChallenges()
  {
    $challenges = $this->data->getSpeedRunChallenges(1);
    
    $this->assertEqual(1, count($challenges));
    
    $challenges = $this->data->getSpeedRunChallenges(2);
    
    $this->assertEqual(2, count($challenges));
  }
  
  function testGetChallengeSum()
  {
    $sum = $this->data->getChallengeSum(1);
    
    $this->assertEqual(20.00, $sum);
  }
  
  function testGetOptionSum()
  {
    $sum = $this->data->getOptionSum(1);
    
    $this->assertEqual(15.00, $sum);
    
    $emptySum = $this->data->getOptionSum(2);
    
    // stupid dynamic languages, null _does_not_ equal 0, you hear me!?
    $this->assertEqual(0.00, $emptySum);
    $this->assertNotNull($emptySum);
  }
}

?>