[HOME](../../../../README.md)
# net:ftp-list

FTP command to provide a listing of remote files.

### Property Summary

| Property | Description |
| -------- | ----------- |
| [files](#propertyfiles) | An array of org.apache.commoons.net.FTPFile objects. | 
| [path](#propertypath) | The remote directory path. | 


### Property Detail
#### files <a name="propertyfiles"></a>

<table style='font-size:smaller'>
      <tr><td><i>Access</i></td><td>READ_ONLY</td></tr>
      <tr><td><i>Required</i></td><td>R/O.</td></tr>
</table>

An array of org.apache.commoons.net.FTPFile objects.
Use something like ${listing.files[0].name} to get a file name (where listing
is this id given to this command). For other properties pleas see
the Apache documentation.

#### path <a name="propertypath"></a>

<table style='font-size:smaller'>
      <tr><td><i>Configured By</i></td><td>ATTRIBUTE</td></tr>
      <tr><td><i>Access</i></td><td>READ_WRITE</td></tr>
      <tr><td><i>Required</i></td><td>Yes.</td></tr>
</table>

The remote directory path.


-----------------------

<div style='font-size: smaller; text-align: center;'>(c) R Gordon Ltd 2005 - Present</div>
