<?php

class DataAccess
{
  private $db;
  
  function __construct($db)
  {
    $this->db = $db;
  }
  
  function getAllSpeedRuns()
  {
    $queryString = "SELECT * FROM SpeedRun;";
    
    $queryResult = $this->db->query($queryString);
    
    $games = $queryResult->fetchAll();
    
    return $games;
  }
  
  function getSpeedRun($id)
  {
    $queryString = "SELECT * FROM SpeedRun WHERE SpeedRun.speedRunId = $id;";
    
    $queryResult = $this->db->query($queryString);
    
    $games = $queryResult->fetchAll();
    
    if (count($games) == 1)
    {
      return $games[0];
    }
    else
    {
      return null;
    }
  }
  
  function getSpeedRunChoices($speedRunId)
  {
    $queryString = "SELECT * FROM Choice WHERE Choice.speedRunId = $speedRunId;\n";
    
    $queryResult = $this->db->query($queryString);
    
    $choices = $queryResult->fetchAll();
    
    return $choices;
  }
  
  function getChoiceOptions($choiceId)
  {
    $queryString = "SELECT * FROM ChoiceOption WHERE ChoiceOption.choiceId = $choiceId;\n";
    
    $queryResult = $this->db->query($queryString);
    
    $options = $queryResult->fetchAll();
    
    return $options;
  }
  
  function getSpeedRunChallenges($speedRunId)
  {
    $queryString = "SELECT * FROM Challenge WHERE Challenge.speedRunId = $speedRunId;\n";
    
    $queryResult = $this->db->query($queryString);
    
    $challenges = $queryResult->fetchAll();
    
    return $challenges;
  }
  
  function getChallengeSum($challengeId)
  {
    $queryString = "SELECT SUM(amount)" .
     " FROM ChallengeBid" .
     " WHERE ChallengeBid.challengeId = $challengeId;";
    
    $queryResult = $this->db->query($queryString);
    
    $queryData = $queryResult->fetchAll();
    
    $sum = $queryData[0][0];
    
    if ($sum == null)
    {
      $sum = 0.00;
    }
    
    return $sum;
  }
  
  function getOptionSum($optionId)
  {
    $queryString = "SELECT SUM(amount)" .
     " FROM ChoiceBid" .
     " WHERE ChoiceBid.optionId = $optionId;";
    
    $queryResult = $this->db->query($queryString);
    
    $queryData = $queryResult->fetchAll();
    
    $sum = $queryData[0][0];
    
    if ($sum == null)
    {
      $sum = 0.00;
    }
    
    return $sum;
  }
  
}