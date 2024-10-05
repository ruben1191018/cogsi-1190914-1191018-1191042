Ant Basic Demo
===================

This is a demo application that implements a basic multithreaded chat room server.

The server supports several simultaneous clients through multithreading. When a client connects the server requests a screen name, and keeps requesting a name until a unique one is received. After a client submits a unique name, the server acknowledges it. Then all messages from that client will be broadcast to all other clients that have submitted a unique screen name. A simple "chat protocol" is used for managing a user's registration/leaving and message broadcast.


Prerequisites
-------------

* Java JDK 17
* Apache Log4J 2
* Apache Ant(TM) version 1.10.15


Compile and Package
-----

    ant jar


Run the server
--------------

Open a terminal and execute the following command from the project's root directory:

    ant runServer


Run a client
------------

Open another terminal and execute the following ant command from the project's root directory:

    ant runClient

The above task assumes the chat server's IP is "localhost" and its port is "59001". If you which to use other parameters please edit the runClient task in the "build.xml" file in the project's root directory.

To run several clients, you just need to open more terminals and repeat the invocation of the ("ant runClient") command.