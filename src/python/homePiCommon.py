#make sure the update dir is present to download into.
def ensure_updatedir(dir):
  d = os.path.join(os.getcwd(),dir)
  print d
  if not os.path.exists(d):
    print "Making Dir %s" % d
    os.makedirs(d)

#Log messages
def logMessage(msg):
  ensure_updatedir("log")
  log = open(os.path.join("log", "running.log"), 'a')
  log.write('\n'+datetime.datetime.now().ctime() + ' ')
  log.write(msg);