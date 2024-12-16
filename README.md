dropDrive
=========

- `dropdrive-cmd` is simple java command line application for uploading file(s) to Dropbox.
- `dropdrive-core` is core library which provide ability for uploading file(s) to Dropbox.

dropDrive as command line application
-------------------------------------

### Usage
usage: `dropdrive [-a <code>] [-d <dir>] [-f <file>] [-h] [-l] [-p <props>] [-v]`

 `-a <code>` - process authorization<br/>
 `-d <dir>` - directory for upload; creates new one if no directory exists; default is dropdrive-uploads<br/>
 `-f <file>` - upload files<br/>
 `-h` - show this help<br/>
 `-l` - display authorization link<br/>
 `-p <props>` - path to dropDrive properties file<br/>
 `-v` -show dropDrive version

### How to authorize application
1. generate authorization URL:<br/>
   `dropdrive -p dropdrive.properties -l`
2. copy and paste URL to your browser to receive authorization code
3. authorize application with received authorization code:<br/>
   `dropdrive -p dropdrive.properties -a <code>`

### How to upload files
Upload file(s) to Dropbox:<br/>
   `dropdrive -p dropdrive.properties -f <file1> <file2> <file3>`

Files are uploaded to `dropdrive-uploads` directory by default.

If you want to change upload directory:

- change `dropdrive.uploadDir` property in _properties_ file
- or pass directory in `-d <dir>` argument:<br/>
   `dropdrive -p dropdrive.properties -f <file> -d <path>/<to>/<dir>`

### How to use properties file
- `dropdrive.accessToken` - Dropbox access token; this property is updated automatically by dropDrive
- `dropdrive.uploadDir` - path to dir where files will be uploaded: `<path>/<to>/<dir>`

dropDrive as core library
-------------------------
dropDrive core can be used in any other application to provide ability for uploading file(s) to Dropbox.

### Usage
1. add implementation of `IDropDriveCredential` interface to spring context
   or use `SimpleDropDriveCredential` or extend `AbstractDropDriveCredential`
2. provide client key as `dropdrive.core.drive.clientKey` property in spring context
3. provide client secret as `dropdrive.core.drive.clientSecret` property in spring context
4. import dropDrive spring context with annotation `@Import(net.czpilar.dropdrive.core.context.DropDriveCoreContext.class)`
5. autowire `IFileService` and use file uploading methods

License
=======

    Copyright 2019 David Pilar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
