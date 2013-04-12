"""
   Manages downloading and updating applications throught he use of a zip file hosted on HomePi server.
"""
import datetime
import os.path
import zipfile
import urllib2

#Test call to verify thins work.
def print_info(archive_name):
    zf = zipfile.ZipFile(archive_name)
    for info in zf.infolist():
        print info.filename
        print '\tComment:\t', info.comment
        print '\tModified:\t', datetime.datetime(*info.date_time)
        print '\tSystem:\t\t', info.create_system, '(0 = Windows, 3 = Unix)'
        print '\tZIP version:\t', info.create_version
        print '\tCompressed:\t', info.compress_size, 'bytes'
        print '\tUncompressed:\t', info.file_size, 'bytes'
        print

# Extracts files from given archive into targetlocation. Preserves archive folder structure.
def extractFiles(archive_name,targetLocation):
    zf = zipfile.ZipFile(archive_name)
    zf.extractall(path=targetLocation)

# Download archive file for unpacking.
def retrieveZipfile(saveLocation, archiveURI, currentVersion = -1):
    fileName = os.path.basename(archiveURI)
    print 'downloading file: %s' % fileName
    try:
        response = urllib2.urlopen(archiveURI)

        #Check to see if new version        
        serverVersion = 0
        if response.info().getheader('file-version') is not None:
            serverVersion = int(response.info().getheader('file-version'))
     
            ##version check, download if new
            if currentVersion< serverVersion:
                fileDest = os.path.join(saveLocation,fileName)
                with open(fileDest, "wb") as code:
                    code.write(response.read())
    	    print 'Download done'       
    except urllib2.HTTPError as h:
    	print 'Error downloading file: %s' % h
    

#Test to see if everything runs smoothly, should add verification and clean up
if __name__ == '__main__':
    #print_info('../test/resources/ansi161.zip')
    
    extractFiles('../test/resources/ansi161.zip', '../test/resources/temp/')
    
    retrieveZipfile( '../test/resources/temp/', 'http://the.earth.li/~sgtatham/putty/latest/x86/putty.zip')