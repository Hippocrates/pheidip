<html>
<head>
<?php 
require_once("ddmanag_config.php");
require_once("db/DataAccess.php");

$data = new DataAccess($donationDB);

$game = null;

if (array_key_exists('game_id', $_GET))
{
  $game = $data->getSpeedRun($_GET['game_id']);
}

$pageTitle = $game == null ? "Error" : $game['name'];

?>
<title><?php echo $pageTitle?></title>
</head>
<body>
<?php
if ($game == null)
{
  echo "No game id supplied. <br />";
}
else
{
  $choices = $data->getSpeedRunChoices($game['speedRunId']);
  $challenges = $data->getSpeedRunChallenges($game['speedRunId']);
  
  if (count($choices) == 0 && count($options) == 0)
  {
  	echo "<h3>There are no choices are challenges submitted for this run.</h3>";
  }

  foreach ($choices as $choice)
  {
    $choiceName = $choice['name'];
    echo "<h3>Choice '$choiceName':</h3>";
    
    $options = $data->getChoiceOptions($choice['choiceId']);
    
    if (count($options) == 0)
    {
      echo "<h4>No options have been submitted for this choice yet.</h4>";
    }
    
    foreach ($options as $option)
    {
      $optionName = $option['name'];
      $optionTotal = number_format($data->getOptionSum($option['optionId']), 2, '.', '');
      
      echo "<h4>$optionName : $$optionTotal </h4>";
    }
  }
  
  echo "";

  foreach ($challenges as $challenge)
  {
    $challengeName = $challenge['name'];
    $challengeTotal = number_format($data->getChallengeSum($challenge['challengeId']), 2, '.', '');
    $challengeGoal = number_format($challenge['goalAmount'], 2, '.', '');
    
    echo "<h3>Challenge '$challengeName':</h3>";
    echo "<h4>Collected $$challengeTotal of $$challengeGoal so far.</h4>";
  }
  echo "";
}

?>
</body>
</html>