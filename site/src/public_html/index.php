<html> 
<head> 
<title>Sample Page</title>
<?php 
require_once("config/ddmanag_init.php");
require_once("db/DataAccess.php");

$data = new DataAccess($donationDB);
?>
</head> 
<body> 

<br />
<?php
$runs = $data->getAllSpeedRuns();

echo "This page desperately needs attention by a real web designer...";

echo "<h3>List of all runs:</h3>";


foreach($runs as $run)
{
  $runId = $run['speedRunId'];
  $runName = $run['name'];
  echo "<a href='game_data.php?game_id=$runId'>$runName</a> <br />";
}
?>
<br />


</body>
</html> 