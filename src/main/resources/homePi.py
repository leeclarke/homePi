#!/usr/bin/env python


"""
  HomePi -  keeping in touch with Home.
  
  The goal of HomePi is to report back to a sort of Pi Mothership, or server, and let the Pi overlord 
  know how his minions are doing out in the field. This includes checking in and saying Hi, logging errors,
  and asking for updates to the Pi's core programs.
  
  To accomplish this task the Pi will periodically check in with Home and upload any important log files.
  Every hour/couple hours/ day it will ask the HomePi "Mothership" if there might be a newer script for 
  it's specific functions.
  
"""

from hpcommon import *
import urllib2, os, time, json
from Configuration import Configuration
from subprocess import Popen

#Read ini file
config = Configuration('homePi.ini')

def getVersion():
  return 2

#Currently Downloads updates only for HomePi but as this script evolves updates for supported apps will be included as well.
def downloadUpdates():
  #Determiine if it's time for an update check  
  updateTime = float(config.main.lastupdate) + (float(config.main.updatefreqhrs) * 3600)
  print "curTime= %f > lastUpdate: %f" % (time.time(), updateTime)
  if time.time() > updateTime:
    logMessage(config.log.logFile, "Checking for software update")
  
    #Download the code from server
    response = urllib2.urlopen(config.updates.downloadurl)
  
    #Check to see if new version
    serverVersion = int(response.info().getheader('file-version'))
  
    if serverVersion > getVersion():
      logMessage(config.log.logFile, "Updating to new version:" + str(serverVersion))
      ensure_dir(config.log.updateFolder)
      localFile = open(os.path.join(config.log.updateFolder, config.main.appmain), 'w')
      localFile.write(response.read())
      localFile.close()

      logMessage(config.log.logFile, 'New updates downloaded.')    
    
      #open the reloader to replace files, clean up and restart
      reloaderPath = "python " + os.path.join(os.getcwd(),config.main.reloader)
      Popen(reloaderPath, shell=True)
    else:
      logMessage(config.log.logFile, "No update, version " + str(getVersion()) + " is the current version.")
      
    #Set updateTime
    config.setValue("main","lastUpdate",str(time.time()))
    print 'HomePi Ready for update.'
    
def main():
  print 'running... HomePi build: %d' % getVersion()
  #Check for HomePi updates.
  downloadUpdates()
  
  #Do other tasks.
  print 'Check PiProfile etc.'
  
  exit("HomePi done.")

if __name__ == '__main__':
    main()