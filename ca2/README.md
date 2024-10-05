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

### Step 2- Add unit test and update gradle build script

In this step, we updated the build.gradle file to include dependencies necessary for unit testing and configured 
the test task to use JUnit 5. Additionally, a basic unit test was implemented to verify the setup.

#### Alterations to the build.gradle file:

To support unit testing, the following dependencies were added to the dependencies section:

    dependencies {
        testImplementation 'org.junit.jupiter:junit-jupiter-api:5.7.0' 
        testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.7.0'
        testImplementation 'org.mockito:mockito-core:3.7.7'
    }

- JUnit Jupiter API (version 5.7.0): Provides the necessary annotations and test functionality.
- JUnit Jupiter Engine (version 5.7.0): Used to run the tests written with JUnit 5.
- Mockito Core (version 3.7.7): Enables mocking during unit tests, allowing for isolated testing of components.

#### Configuration of the test task:

    test {
        useJUnitPlatform()
        testLogging {
            events "passed", "skipped", "failed"
        }
    }

- useJUnitPlatform(): Ensures that the tests are executed using JUnit 5.
- testLogging: Logs details of test execution, including passed, skipped, and failed tests.

#### Unit Test Class Implementation:

To validate the test setup, a basic test class was added:

    package basic_demo;

    import org.junit.jupiter.api.Test;
    import static org.junit.jupiter.api.Assertions.*;

    class ChatClientTest {

        @Test
        void run() {
            assertTrue(true);
        }
    }

This simple test checks the validity of the setup by asserting that a true statement is valid.


To execute the tests, the following Gradle command can be used:

    ./gradlew test

This command runs all unit tests in the project and outputs the results, showing which tests passed, were skipped, or failed, in accordance with the logging settings defined in the test task.

### Step 3 - Create a new task of type Copy

To create the new task to execute the server we edited the build.gradle file
and added the following task named backup

    task backup(type: Copy) {
        from 'src'
        
        into "backup"
    }

This a task of type Copy wich allows Gradle to copy a folder.

- from: This defines the source directory that the task will copy from. In this case, it's the src folder, which contains the source code of the project.
- into: This sets the destination directory where the contents of the src folder will be copied. In this case, it will copy everything into a folder named backup in the project’s root directory.

For the backup to be executed, using the gradle task, we can use the following gradle command:

    ./gradlew backup