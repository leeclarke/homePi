#!/usr/bin/env python

from hpcommon import *
import urllib2, os, datetime, json
from Configuration import Configuration
from subprocess import Popen

def getVersion():
  return 1

print 'HomePi version: %d' % getVersion()



