#!/usr/bin/env python

#TODO: ADD TO CronTab

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
import getopt,sys,subprocess,urllib2, os, time, json
from Configuration import Configuration
from subprocess import Popen

helpInfo = 'homePi.py -a <appName> -m <logmessage> | -f <logFilePath>  \nOr call no-op for standard function.'

#Read ini file
config = Configuration('homePi.ini')

def getVersion():
  return 4

#Currently Downloads updates only for HomePi but as this script evolves updates for supported apps will be included as well.
def downloadUpdates():
  #Determine if it's time for an update check  
  updateTime = float(config.main.lastupdate) + (float(config.main.updatefreqhrs) * 3600)
  print "curTime= %f > lastUpdate: %f" % (time.time(), updateTime)
  if time.time() > updateTime:
    logMessage(config.log.logFile, "Checking for software update")
  
    #Download the code from server
    response = urllib2.urlopen(config.updates.downloadurl+ "?piSerial=" + getPiSerial())
  
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

# Execute commands requested by args provided.
def processCommands(argv):
  try:
    opts, args = getopt.getopt(argv,"ha:m:f:")
  except getopt.GetoptError:
    print helpInfo
    sys.exit(2)
      
  appName, logmessage,filePath = parseArgs(opts)
  
  #DEBUGING print "appName= %s , logmessage= %s filePath= %s"  % (appName,logmessage,filePath)

#TODO: Finish by adding the logging API calls. and HomePi logging
  if appName != None:
    if logmessage != None:
      # call log msg
      print logmessage
    elif filePath != None:
      p = Popen(['tail', '-n40', filePath], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
      out, err = p.communicate()
      #call log message
      print out
    else:
      print helpInfo
    sys.exit(2)
    
  print 'No opts'

#parses any provides arguments and returns values if present
def parseArgs(opts):
  appName = None
  logmessage = None
  filePath = None
  
  for opt, arg in opts:
    if opt == '-h':
      print helpInfo 
      sys.exit()
    elif opt in ("-a"):
      appName = arg
    elif opt in ("-m"):
      logmessage = arg
    elif opt in ("-f"):
      filePath = arg
   
  return appName, logmessage,filePath  
    
### Main    
def main(argv):
  print 'running... HomePi build: %d' % getVersion()
  #Special processing of command args if present
  processCommands(argv)
  
  #Check for HomePi updates.
  downloadUpdates()
  
  #Do other tasks.
  print 'Check PiProfile etc.'
  
  exit("HomePi done.")

if __name__ == '__main__':
    main(sys.argv[1:])