<?php

require_once("simpletest/autorun.php");
require_once("test/db/DonationTestDB.php");

class TestDonationSchema extends UnitTestCase
{
  private $db;
  
  function setUp()
  {
    $this->db = createDonationTestDB();
  }
  
  function tearDown()
  {
    $this->db = null;
  }
  
  function testInsert()
  {
    $insertUpdateCount = $this->db->exec("INSERT INTO Donor VALUES(1, 'e@d.c', null, '', '');");
    $insertUpdateCount = $this->db->exec("INSERT INTO Donor VALUES(2, 'test4@test.com', null, '', '');");
    
    $this->assertEqual(1, $insertUpdateCount);
    
    $queryResult = $this->db->prepare("SELECT * FROM Donor WHERE Donor.donorId = 1;");
    
    $queryResult->execute();
    
    $this->assertNotNull($queryResult);
    
    if ($queryResult != null)
    {
      $list = $queryResult->fetchAll();
      
      $this->assertEqual(1, count($list));
      
      if (count($list) == 1)
      {
        $row = $list[0];
        
        $this->assertEqual(1, $row['donorId']);
        $this->assertEqual('e@d.c', $row['email']);
      }
    }
  }
}

?>