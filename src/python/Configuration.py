## {{{ http://code.activestate.com/recipes/426406/ (r1)
from ConfigParser import SafeConfigParser
import logging, os

clog = logging.getLogger("Configuration")

#def getVersion():
#  return 1

class Configuration:
    def __init__ (self, fileName):
        self.__checkForFile__(fileName)
        cp = SafeConfigParser()
        cp.read(fileName)
        self.__parser = cp
        self.fileName = fileName
        
    def __getattr__ (self, name):
        if name in self.__parser.sections():
            return Section(name, self.__parser)
        else:
            return None
            
    def __str__ (self):
        p = self.__parser
        result = []
        result.append('<Configuration from %s>' % self.fileName)
        for s in p.sections():
            result.append('[%s]' % s)
            for o in p.options(s):
                result.append('%s=%s' % (o, p.get(s, o)))
        return '\n'.join(result)

    def __checkForFile__ (self,fileName):
        try:
            with open(fileName) as f: pass
        except IOError as e:
            clog.error('file not found %s ' % fileName)
            raise IOError('file not found %s ' % fileName)
    def setValue(self, section, name, value):
        self.__parser.set(section, name, value)
        with open(self.fileName, 'w') as configfile:  #Save cahnge
            self.__parser.write(configfile)
class Section:
    def __init__ (self, name, parser):
        self.name = name
        self.__parser = parser
    def __getattr__ (self, name):
        return self.__parser.get(self.name, name)
    #def __setattr__(self, name, value):
    #    self.__parser.set(self.name, name, value)
        

# Test
if __name__ == '__main__':
    configFile = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'homePi.ini')
    c = Configuration(configFile)
   # print c.log.logFile, c.log.updateFolder
    
    c.setValue("main","lastUpdate","2")
    # An extra: print the configuration object itself
    print c
## end of http://code.activestate.com/recipes/426406/ }}}
