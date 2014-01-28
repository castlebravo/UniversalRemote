UniversalRemote
===============

Repository for the universal remote app


Author: Chris McCarty


This is the Universal Remote Android Application source code and Universal Remote Server source code.
The Android Application was written in the eclipse SDK for Android. The Server was written in the NetBeans IDE.
The Android Application will stream accelerometer, gyroscope, and magnetometer data to the Server via Java
DatagramSockets. The Server will receive this data, and plot it on an HTML page using JavaScript. Specifically,
the Google Charts API is used to provide visualizations of the data, as well as make queries for updated data
at regular intervals from a different servlet of the Server itself.

The Server GUI asks for the IP address only to provide it to the JavaScript queries back to itself from the 
Google Chart. If this is not set correctly, the Google Charts will be incapable of receiving data from the server.
The "get default values" button will attempt to find the correct IP by iterating through all the network devices
listed on the machine. This has been tested and works on a machine running Ubuntu 12.04. 

Both of these programs are works in progress and are by no means complete. The current release acts as a framework
from which network applications requiring real time sensor data can be developed.

test test
