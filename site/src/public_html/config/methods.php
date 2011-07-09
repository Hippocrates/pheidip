<?php

function checkNumberParam($number)
{
 $asInt = intval($number);
 return "$asInt" == $number;
}

?>