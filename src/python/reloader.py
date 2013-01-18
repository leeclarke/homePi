#!/usr/bin/env python

from hpcommon import *
import logConfig,logging
from shutil import copy2, rmtree, os
from Configuration import Configuration
from subprocess import Popen
from sys import exit

def getVersion():
  return 1

runLogger = logging.getLogger("homePi.reloader")

#Read ini file
configFile = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'homePi.ini')
runLogger.debug("configFile: %s" % configFile)
config = Configuration(configFile)


# Copy files to replace runtime file
try:
  copy2(os.path.join(config.log.updateFolder, config.main.appmain), config.main.appmain) # copy file
except IOError as e:
  runLogger.error(config.log.logFile,  e)
  print e

rmtree(config.log.updateFolder) # will delete the folder itself


mainPath = "python " + os.path.join(os.getcwd(),config.main.appmain)
runLogger.debug("mainPath: %s" % mainPath)
Popen(mainPath, shell=True) # go back to your program

runLogger.debug('New updates installed.')
exit("HomePi Updated. Restarting HomePi...")