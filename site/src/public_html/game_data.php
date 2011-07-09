<html>
<head>
<?php 
require_once("config/ddmanag_init.php");
require_once("db/DataAccess.php");

$data = new DataAccess($donationDB);

$game = null;

if (array_key_exists('game_id', $_GET))
{
  $id = $_GET['game_id'];
  if (checkNumberParam($id))
  {
  	$game = $data->getSpeedRun();
  }
}

$pageTitle = $game == null ? "Error" : $game['name'];

?>
<title><?php echo $pageTitle?></title>
</head>
<body>
<?php
if ($game == null)
{
  echo "<h3>Invalid game id supplied. <br /></h3>";
}
else
{
  echo "<h3>Information for " . $game['name'] . "</h3>";
	
  $choices = $data->getSpeedRunChoices($game['speedRunId']);
  $challenges = $data->getSpeedRunChallenges($game['speedRunId']);
  
  if (count($choices) == 0 && count($options) == 0)
  {
  	echo "<h4>There are no choices are challenges submitted for this run.</h4>";
  }

  foreach ($choices as $choice)
  {
    $choiceName = $choice['name'];
    echo "<h4>Choice '$choiceName':</h4>";
    
    $options = $data->getChoiceOptions($choice['choiceId']);
    
    if (count($options) == 0)
    {
      echo "<h5>No options have been submitted for this choice yet.</h5>";
    }
    
    foreach ($options as $option)
    {
      $optionName = $option['name'];
      $optionTotal = number_format($data->getOptionSum($option['optionId']), 2, '.', '');
      
      echo "<h5>$optionName : $$optionTotal </h5>";
    }
  }
  
  echo "";

  foreach ($challenges as $challenge)
  {
    $challengeName = $challenge['name'];
    $challengeTotal = number_format($data->getChallengeSum($challenge['challengeId']), 2, '.', '');
    $challengeGoal = number_format($challenge['goalAmount'], 2, '.', '');
    
    echo "<h3>Challenge '$challengeName':</h3>";
    echo "<h4>Collected $$challengeTotal" . ($challengeGoal > 0 ? " of $$challengeGoal" : "") . " so far.</h4>";
  }
  echo "";
}

?>
</body>
</html>