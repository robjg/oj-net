[HOME](../../../../README.md)
# net:ftp

Connect to an FTP server and run a set of FTP commands.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [commands](#propertycommands) | A List of the FTP commands. | 
| [host](#propertyhost) | The FTP server. | 
| [name](#propertyname) | The name of this Job. | 
| [passive](#propertypassive) | True if the connection is passive. | 
| [password](#propertypassword) | The password to connect to the FTP server with. | 
| [port](#propertyport) | The Port. | 
| [result](#propertyresult) |  | 
| [username](#propertyusername) | The user name to connect to the FTP server with. | 


### Example Summary

| Title | Description |
| ----- | ----------- |
| [Example 1](#example1) | Doing lots of FTP things. |


### Property Detail
#### commands <a name="propertycommands"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>WRITE_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

A List of the FTP commands.

#### host <a name="propertyhost"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>Yes.</td></tr>
</table>

The FTP server.

#### name <a name="propertyname"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The name of this Job.

#### passive <a name="propertypassive"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No, defaults to false.</td></tr>
</table>

True if the connection is passive.

#### password <a name="propertypassword"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>Yes, unless user is anonymous.</td></tr>
</table>

The password to connect to the FTP server with.

#### port <a name="propertyport"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>No.</td></tr>
</table>

The Port.

#### result <a name="propertyresult"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
</table>



#### username <a name="propertyusername"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>Yes.</td></tr>
</table>

The user name to connect to the FTP server with.


### Examples
#### Example 1 <a name="example1"></a>

Doing lots of FTP things.

```xml
<oddjob xmlns:net='http://rgordon.co.uk/oddjob/net' 
        id="this">
    <job>
        <net:ftp host='localhost' 
                 port='${ftp.port}'
                 username='admin' 
                 password='admin'>
            <commands>
                <list>
                    <values>
                        <identify id='pwd'>
                            <value>
                                <net:ftp-pwd/>
                            </value>
                        </identify>
                        <net:ftp-mk-dir path='stuff'/>
                        <net:ftp-cd path='stuff'/>
                        <identify id='list1'>
                            <value>
                                <net:ftp-list/>
                            </value>
                        </identify>
                        <net:ftp-ascii/>
                        <net:ftp-put remote='stuff.txt'>
                            <file>
                                <file file='${this.args[0]}'/>
                            </file>
                        </net:ftp-put>
                        <identify id='list2'>
                            <value>
                                <net:ftp-list/>
                            </value>
                        </identify>
                        <net:ftp-rename from='stuff.txt'
                                        to='things.txt'/>
                        <net:ftp-get remote='things.txt'>
                            <file>
                                <file file='${this.args[1]}'/>
                            </file>
                        </net:ftp-get>
                        <net:ftp-delete path='things.txt'/>
                        <net:ftp-bin/>
                        <net:ftp-cd/>
                        <net:ftp-rm-dir path='stuff'/>
                    </values>
                </list>
            </commands>
        </net:ftp>
    </job>
</oddjob>
        

```



-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
