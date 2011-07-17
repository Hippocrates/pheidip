<html>
<head>
<?php 
require_once("config/ddmanag_init.php");
require_once("db/DataAccess.php");
require_once("config/methods.php");

$data = new DataAccess($donationDB);

$game = null;

if (array_key_exists('game_id', $_GET))
{
  $id = $_GET['game_id'];
  if (checkNumberParam($id))
  {
  	$game = $data->getSpeedRun($id);
  }
}

$pageTitle = $game == null ? "Error" : $game['name'];

?>
<title><?php echo $pageTitle?></title>

<link rel="stylesheet" type="text/css" href="game_data.css" />
</head>
<body>
<?php
if ($game == null)
{
  echo "<h3>Invalid game id supplied.</h3>";
}
else
{
  echo "<h3>" . $game['name'] . "</h3>";

  $choices = $data->getSpeedRunChoices($game['speedRunId']);
  $challenges = $data->getSpeedRunChallenges($game['speedRunId']);
  
  if (count($choices) == 0 && count($challenges) == 0)
  {
    echo "<h4>Nothing availiable for this game yet.</h4>";
  }
  
  if (count($choices) > 0)
  {
    foreach ($choices as $choice)
    {
      $choiceName = $choice['name'];
      $choiceHeader = "$choiceName:" . (($choice['bidState'] == 'CLOSED') ? " (Closed)" : "");
      echo "<h4>$choiceHeader</h4>";

      $options = $data->getChoiceOptions($choice['choiceId']);
      
      if (count($options) == 0)
      {
        echo "<h4>&nbsp;No options have been submitted for this choice yet.</h4>";
      }
      else
      {
        echo "<table>";
        echo "<tr><td>Option Name</td><td>Total Collected</td></tr>";
        foreach ($options as $option)
        {
          $optionName = $option['name'];
          $optionTotal = number_format($data->getOptionSum($option['optionId']), 2, '.', '');
        
          echo "<tr><td>$optionName</td><td>$$optionTotal</td><tr>";
        }
        echo "</table><br />";
      }
    }
  }
  
  if (count($challenges) > 0)
  {
    echo "<h4>Challenges:</h4>";
    echo "<table>";
    echo "<tr><td>Name</td><td>Total Collected</td><td>Goal Amount</td><td>Description</td></tr>";
    
    foreach ($challenges as $challenge)
    {
      $challengeName = $challenge['name'];
      $challengeTotal = number_format($data->getChallengeSum($challenge['challengeId']), 2, '.', '');
      $challengeGoal = number_format($challenge['goalAmount'], 2, '.', '');
      
      $challengeHeader = "$challengeName" . (($challenge['bidState'] == 'CLOSED') ? " (Closed)" : "");
      $challengeGoalText = ($challengeGoal > 0 && $challengeGoal != null) ? "$$challengeGoal" : "";
      $challengeDescription = $challenge['description'];
      echo "<tr><td>$challengeHeader</td><td>$$challengeTotal</td><td>$challengeGoalText</td><td>$challengeDescription</td></tr>";
    }
    
    echo "</table><br />";
  }
}

?>
</body>
</html>