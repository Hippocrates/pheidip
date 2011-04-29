<?php

require_once("db/DBConfig.php");

function createDonationTestDB()
{
  $db = new PDO("sqlite::memory:");
  
  $tableSchema = file_get_contents(getDonationSchemaFilename());
  
  $createResult = $db->exec($tableSchema);
  
  return $createResult ? $db : null;
}

?>