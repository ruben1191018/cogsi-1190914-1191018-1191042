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

To create the new task to do a backup we edited the build.gradle file
and added the following task named backup

    task backup(type: Copy) {
        from 'src'
        
        into "backup"
    }

This a task of type Copy which allows Gradle to copy a folder.

- from: This defines the source directory that the task will copy from. In this case, it's the src folder, which contains the source code of the project.
- into: This sets the destination directory where the contents of the src folder will be copied. In this case, it will copy everything into a folder named backup in the project’s root directory.

For the backup to be executed, using the gradle task, we can use the following gradle command:

    ./gradlew backup


### Step 4 - Create a new task of type Zip

To create the new task to zip the backup directory we edited the build.gradle file
and added the following task named zipBackup

    task zipBackup(type: Zip) {
        dependsOn backup
    
        from "backup"
    
        destinationDirectory = file("archives")
    
        archiveFileName = "backup.zip"
    }


This a task of type Zip which allows Gradle zip a directory.

- dependsOn backup: This indicates that the zipBackup task depends on the execution of the backup task. Before creating the ZIP file, Gradle will first run the backup task, which copies the source files into the backup folder.
- from "backup": This specifies the source directory that the zipBackup task will include in the ZIP archive. Here, it's using the backup folder, which is generated by the backup task.
- destinationDirectory = file("archives"): This defines where the ZIP file will be saved. In this case, the ZIP file will be stored in an archives directory, which will be created inside the project root if it doesn't already exist.
- archiveFileName = "backup.zip": This sets the name of the resulting ZIP file. The archive will be named backup.zip and will be stored in the archives folder as defined above.

For the zip to be executed, using the gradle task, we can use the following gradle command:

    ./gradlew zipBackup

It wasn't necessary to manually download and install specific versions of Gradle and the JDK for this application because the project uses Gradle's Wrapper and Java toolchains.

Gradle Wrapper: This feature allows the project to include a specific version of Gradle in the project itself. When we run ./gradlew (the Gradle wrapper script), it automatically downloads the correct version of Gradle for the project if it's not already available on the machine. This ensures the build always uses the same Gradle version, avoiding compatibility issues.

Java Toolchains: Java toolchains in Gradle make it possible to define the Java version required to compile, test, and run the project, even if the correct JDK is not installed locally. Gradle will automatically download and use the appropriate version of the JDK if it’s not available.

When we run the command gradle -q javaToolchain, Gradle outputs details about the Java toolchain being used. This includes the version of the JDK used by Gradle, confirming that you don’t need to manually manage the JDK for this project.


![img.png](gradletoolchain.png)

## Part 1 - Alternative(Ant)

### Step 0 - Comparison between Ant and Gradle in Build Automation

#### Build Automation Features

Gradle and Apache Ant are both popular build tools for Java applications, but they offer distinct approaches to automation, flexibility, and extensibility.

1 - Declarative vs. Procedural Approach:

- Gradle: Gradle uses a declarative approach to build automation, which means that the build 
file (typically written in Groovy or Kotlin) describes what to do. Gradle implicitly handles 
dependencies between tasks and automatically applies best practices for task execution.
  - Example: In the Gradle build file, we defined tasks like runServer and runClient, and 
Gradle automatically handled the task execution order and classpath setup based on project configuration.
- Ant: Ant, on the other hand, follows a procedural approach, meaning that you explicitly 
define how each step should be executed. Every action, such as compiling code or creating 
directories, must be specified explicitly.
  - Example: In the Ant build file, we had to manually specify the directories for compiled 
classes, explicitly call javac to compile code, and define classpaths in various targets. 
Ant also requires manual definition of dependencies between tasks (e.g., the runServer 
target depends on the jar target).

2 - Extensibility and Plugins:

- Gradle: Gradle offers a wide range of plugins that extend its functionality, making it 
highly flexible. For example, plugins for Java, Kotlin, Android, and more are easily integrated 
into a build script with a single line of configuration.
    - Example: In the provided Gradle build file, the application plugin is used to simplify 
the task of building and running Java applications, which includes configuring the main class 
and handling dependencies like Log4J.
- Ant: Ant does not have a formal plugin system like Gradle. However, you can extend it by 
writing custom Ant tasks or incorporating external libraries. Writing custom tasks in Ant 
requires additional boilerplate code.
    - Example: To implement custom logic in Ant, you would need to write Java classes 
extending Ant’s Task class, whereas in Gradle, writing new tasks is as simple as adding 
them to the build script using the task keyword.
  
3 - Dependency Management:

- Gradle: Gradle has built-in dependency management, relying on Maven or Ivy repositories. 
It handles transitive dependencies and ensures that the correct versions of libraries are used.
  - Example: In the Gradle file, we defined dependencies like Log4J and JUnit directly using
the implementation and testImplementation configurations, and Gradle fetched them from Maven 
Central automatically.
- Ant: Ant lacks built-in dependency management. You need to manually manage dependencies 
by downloading the libraries and including them in the lib folder or specifying their location.
  - Example: In the Ant file, we had to manually place JAR files (Log4J and JUnit) in the 
lib directory and add them to the classpath.

#### Extending with New Functionality

Both tools can be extended with new tasks or features, but the process is more streamlined in Gradle.

- Gradle:
  - Adding new functionality in Gradle is simple. You can define new tasks using the task 
keyword, and existing tasks can be modified or extended with task configuration blocks.
  - Example: We defined new tasks like runServer and runClient directly in the Gradle build file.
Each task was configured with a description, classpath, and arguments without any need to write 
additional Java code or create custom task classes.
  - Gradle’s plugin ecosystem further simplifies adding complex functionality like Docker 
integration, code quality tools, or continuous integration support.
- Ant:
  - Ant allows the creation of new tasks, but it typically requires writing a custom task 
class in Java and registering it in the build file. This makes extending Ant more complex 
than Gradle.
  - Example: In the Ant build file, we had to explicitly define every task using the 
<target> element and manage dependencies between tasks using depends. To create more advanced 
tasks, I would need to write custom Java code.
  - While Ant has some prebuilt tasks and libraries, integrating complex functionality 
often requires much more manual configuration than in Gradle.
  
#### Comparison of Task Definition

- Gradle: Tasks are defined declaratively and can be reused or configured dynamically with 
minimal code. For example, the runClient and runServer tasks were defined with just a few 
lines of configuration using the JavaExec task type.

- Ant: Tasks in Ant are defined procedurally using <target> elements. Each task must be 
manually defined, and the build script tends to grow significantly as more functionality is 
added. For instance, the runServer task required explicit classpath and argument management.

#### Applying Ant to Solve the Same Goals

If we were to solve the same goals using Ant (as outlined in this assignment), the process 
would be as follows:

1 - Building the Application:

- We would need to define a compile target that compiles the Java source code, and a jar 
target to package the compiled classes into a JAR file. This was implemented in the Ant 
build file with explicit use of <javac> and <jar> tasks.

2 - Running the Server and Client:

- In Ant, the runServer and runClient tasks were defined using the <java> task, with 
classpaths and arguments manually specified. These tasks launch the respective classes 
with the correct arguments (e.g., 59001 for the server port).

3 - Unit Testing:

- We used the <junit> task in Ant to run unit tests. The classpath for test execution had 
to be manually managed, and test reports were generated in a specified directory. This 
approach is more manual than Gradle’s built-in test task, which uses the JUnit platform 
out of the box.

4 - Backup and Archiving:

- In Ant, we created a backup target that copies the source code to a backup directory, 
followed by a zipBackup target that creates a ZIP archive. These tasks were implemented 
using <copy> and <zip>, which required explicit file handling and directory setup, unlike 
the simplified Copy and Zip tasks in Gradle.

#### Conclusion of Comparison between Ant and Gradle in Build Automation

Overall, Gradle offers a more modern, declarative, and efficient way to manage build automation 
compared to Ant. It simplifies dependency management, extends functionality easily via plugins, 
and offers a clean way to define tasks without needing boilerplate. Ant, while powerful and 
flexible, requires more manual setup, making it less efficient for large or complex projects 
where Gradle’s features shine.

While Ant can solve the same goals as Gradle (as demonstrated in this assignment), Gradle is 
generally more suitable for modern projects, especially with its better support for complex 
builds, plugins, and automated dependency management.


### Step 1- Create Ant task to execute server

To execute the chat server in Ant, we created a custom target in the build.xml file named 
runServer. This target is responsible for compiling the project, packaging it into a JAR 
file, and then running the chat server. Here's the breakdown of how the task was implemented:

    <target name="runServer" depends="jar">
        <java classname="basic_demo.ChatServerApp" fork="true" failonerror="true">
            <classpath>
                <pathelement path="${jar.dir}/basic_demo.jar"/>
                <fileset dir="${lib.dir}">
                    <include name="log4j-api-2.24.1.jar"/>
                    <include name="log4j-core-2.24.1.jar"/>
                </fileset>
            </classpath>
            <arg value="59001"/> <!-- Server Port -->
        </java>
    </target>

- depends="jar": This ensures that the server will only run after the project has been compiled and the JAR file is created.
- classname="basic_demo.ChatServerApp": This specifies the fully qualified name of the class containing the main() method that launches the server.
- fork="true": Ensures that the Java process is launched in a separate process, allowing for better process control.
- Classpath: The necessary libraries, including Log4J, are added to the classpath to ensure the server runs without issues. This is similar to how Gradle’s sourceSets.main.runtimeClasspath is set up.
- arg value="59001": This argument specifies the port number the server will use.

To run the server, you just need to use the following command:

    ant runServer

### Step 2- Adding Unit Tests and Configuring Ant for JUnit

Next, we set up unit testing using JUnit in Ant. For this, we created a target named test 
which compiles the test classes and runs them using the junit task:


    <target name="test" depends="compileTests">
        <mkdir dir="${build.dir}/test-reports"/>
        <junit printsummary="on" haltonfailure="yes" haltonerror="yes" fork="true" showoutput="true">
            <classpath>
                <pathelement path="${classes.dir}"/>
                <pathelement path="${test.classes.dir}"/>
                <fileset dir="${lib.dir}">
                    <include name="junit-platform-console-standalone-1.9.2.jar"/>
                </fileset>
            </classpath>
            <formatter type="plain"/>
            <batchtest todir="${build.dir}/test-reports">
                <fileset dir="${test.classes.dir}">
                    <include name="**/*Test.class"/> <!-- Include all test classes -->
                </fileset>
            </batchtest>
        </junit>
    </target>

- JUnit Platform: The standalone JUnit platform is added to the classpath to execute the tests, which is similar to how JUnit is configured in Gradle using the useJUnitPlatform() function.
- Test Logging: The test results are printed, and the task halts on failure or error, providing a quick summary, just like in Gradle with testLogging.
- Batch Test: It scans the compiled test classes (*Test.class) and runs all of them.

To run the unit tests, we used:

    ant test

### Step 3: Creating a Backup Task

To ensure that the project’s source code is properly backed up, we created a task named 
backup. This task copies the source files into a separate backup directory. The configuration 
is as follows:

    <target name="backup" description="Backup the source files">
        <mkdir dir="backups"/> <!-- Ensure backup directory exists -->
        <copy todir="backups">
            <fileset dir="${src.dir}">
                <include name="**/*.java"/> <!-- Include all Java source files -->
            </fileset>
        </copy>
        <echo message="Backup completed! Sources are copied to the backup directory."/>
    </target>

- mkdir: Creates the backups directory if it doesn't exist already.
- copy: Copies all the .java files from the source directory (src/main/java) into the newly created backups directory.
- echo: Prints a confirmation message once the backup process is completed.

We ran this task with:

    ant backup

### Step 4: Creating a Zip Archive of the Backup

To compress the backup into a ZIP file, we added a task named zipBackup. 
This task zips the backups directory into an archive file, ensuring that the backup can 
be stored more efficiently:

    <target name="zipBackup" depends="backup" description="Create a zip archive of the backup">
        <zip destfile="backup.zip" basedir="backups"/>
        <echo message="Backup archive created at ${backup.zip}."/>
    </target>

- depends="backup": The zipBackup task depends on the backup task, ensuring that the backup is created before it's zipped.
- zip: Compresses the backups directory into a ZIP file called backup.zip.
- echo: Outputs a message confirming that the archive was successfully created.

To create the ZIP archive, we ran:

    ant zipBackup

### Java Toolchains and JDK Compatibility

Unlike Gradle's automatic handling of Java toolchains, in Ant we manually specified the 
Java version to ensure compatibility across environments. The following lines in the javac 
task ensure that Java 17 is used for compiling both the source code and the tests:

    <compilerarg value="--release"/>
    <compilerarg value="17"/>

This guarantees that the project is compiled using Java 17, similar to the way Gradle’s Java 
toolchains handle the JDK version management automatically.
