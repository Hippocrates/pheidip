<?php

function getRootDirectory()
{
  return dirname(__FILE__) . "/../";
}

function getDatabaseRootDirectory()
{
  return getRootDirectory() . "../database/";
}