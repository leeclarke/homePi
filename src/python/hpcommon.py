import os, datetime

def getBuild():
  return 1
  
#make sure the update dir is present to download into.
def ensure_dir(dir):
  d = os.path.join(os.getcwd(),dir)
  print d
  if not os.path.exists(d):
    print "Making Dir %s" % d
    os.makedirs(d)

#Log messages
def logMessage(logFile, msg):
  ensure_dir("log")
  log = open(os.path.join("log", logFile), 'a')
  log.write('\n['+datetime.datetime.now().ctime() + '] ')
  log.write(msg)
  
#Retrieves the Pi's UID
def getPiSerial():
  # Extract serial from cpuinfo file
  cpuserial = "0000000000000000"
  try:
    f = open('/proc/cpuinfo','r')
    for line in f:
      if line[0:6]=='Serial':
        cpuserial = line[10:26]
    f.close()
  except:
    cpuserial = "ERROR000000000"
  
  return cpuserial
  

#TODO: make this a real test?
# Test
if __name__ == '__main__':
  print getPiSerial()