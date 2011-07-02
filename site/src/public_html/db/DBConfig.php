<?php

require_once("DirConfig.php");

function getDonationSchemaFilename()
{
  return getDatabaseRootDirectory() . "schema/donation_db.sql";
}

?>