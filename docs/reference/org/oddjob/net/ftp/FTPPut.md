[HOME](../../../../README.md)
# net:ftp-put

FTP command to transfer a file to the server.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [file](#propertyfile) | A file to copy remotely. | 
| [input](#propertyinput) | An input to copy remotely. | 
| [remote](#propertyremote) | The remote name of the file. | 


### Property Detail
#### file <a name="propertyfile"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>Either this or an input is required.</td></tr>
</table>

A file to copy remotely.

#### input <a name="propertyinput"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ELEMENT</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>Either this or a file is required.</td></tr>
</table>

An input to copy remotely.

#### remote <a name="propertyremote"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>Not if a file is used for input.</td></tr>
</table>

The remote name of the file.


-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
