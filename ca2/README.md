# CA2 Report

## Part1 

### Step 1- Create gradle task to execute server

To create the new task to execute the server we edited the build.gradle file
and added the following task named runServer

    task runServer(type:JavaExec, dependsOn: classes){
        group = "DevOps"
        description = "Launches a chat server that can be connected by clients on localhost:59001 "
        
            classpath = sourceSets.main.runtimeClasspath
        
            mainClass = 'basic_demo.ChatServerApp'
            args '59001'
    }


This a task of type JavaExec wich allows Gradle to execute a Java Program

- dependsOn: classes: This specifies that the runServer task depends on the classes task. The classes task compiles the project's source code, ensuring that the Java classes are built before attempting to run the application.
- group = "DevOps": This groups the task under the "DevOps" category, making it easier to organize and locate within Gradle's task listing.
- classpath = sourceSets.main.runtimeClasspath: This defines the classpath that the Java program will use when it runs.
- mainClass = 'basic_demo.ChatServerApp': This specifies the fully qualified name of the Java class that contains the main method to be executed
- args '59001': This provides arguments to the main method of the ChatServerApp class, in this case the port number the server will run

For the server to be executed, using the gradle task, we can use the following gradle command: 

    ./gradlew runServer