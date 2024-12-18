## Part 2

### Step 1 Convert building Rest Services to Gradle (instead of Maven)

In the empty directory we used the gradle init command to create a Gradle Project
The following options were shown:

![img.png](../pictures/gradleinit1.png)

We selected "Application" (1)

![img.png](../pictures/gradleinit2.png)

Then selected "Java" (1)

![img_1.png](../pictures/gradleinit3.png)

For the application structure we went with Single Application Project (1) since we don't want to create a library
We used Groovy (2) as the build script DSL following the same structure as the first part

For the test framework we used JUnit 4 (1) following the same approach as the first part


After having the base Gradle project we replaced the *src* folder with the one from the Building Rest Services

Some dependencies were missing, so we added the same dependencies from the pom.xml into the build.gradle file:

![img.png](../pictures/gradle_dependencies.png)


We had to add plugins and dependencies

The plugins section in Gradle is used to apply plugins to the project. Plugins extend the capabilities of the build system by adding tasks, conventions, and behavior. Plugins can be used for different purposes, like building Java applications, managing dependencies, deploying applications, running tests, and more.

We added "org.springframework.boot", which helps building and packaging Spring Boot Applications, it adds useful tasks like bootRun, bootJar...
We also added "io.spring.dependency-management" which helps manage dependency versions in a more centralized way
And also added "java" which applies the Java Plugin, that adds tasks to compile Java code (compileJava), runTests(test) and package the project into a Jar (jar)

After adding the plugins we added the same dependencies present in the pom.xml


### Step 2 - Create Custom task to zip source code and store in back up directory

To zip source code and store in back up directory we added the new following task called backupSource:

![img.png](../pictures/backupSourceZip.png)

This task is of type Zip meaning it compresses files into a zip file

It takes all the files in the src directory

And the zip file is saved in a directory called backups

Before zipping, the doFirst block cleans up the old backups and recreates a new directory

After the task finishes, the doLast block prints a message indicating the source code has been backed up along with the location of the zip file

### Step 3 - Create a custom task that depends on the installDist task and runs the application using the generated distribution scripts

To do this we added the new following task called runDistApp:

    task runDistApp(type: Exec){
        group = "Application"
        description = "Runs the application using the generated distribution scripts."
    
        dependsOn installDist
    
        def os = org.gradle.internal.os.OperatingSystem.current()
    
        def appDir = "$buildDir/install/${project.name}/bin"
        def execScript
    
        if (os.isWindows()) {
            execScript = "${appDir}/${project.name}.bat"
        } else {
            execScript = "${appDir}/${project.name}"
        }
    
        println "Running the application using: $execScript"
    
        executable = execScript
    }

- dependsOn installDist: This ensures that the installDist task is executed before the custom task (runDistApp). The installDist task generates the necessary distribution files and scripts under the build/install directory.

![img.png](../pictures/img.png)

- org.gradle.internal.os.OperatingSystem.current(): This Gradle API helps in detecting the current operating system, which is used to choose the correct executable script (either .bat for Windows or .sh for Unix-based systems).

- appDir: This variable contains the path to the generated distribution scripts

- Determine Script Path: Based on the operating system, the script to run is selected. For Windows, it's a .bat file, and for other OSes, it's a .sh file.

- executable: Since this is a Exec Type, we define the correct script file as the executable.

To run this:

./gradlew runDistApp

### Step 4 - Create a custom task that depends on the javadoc task, which generates the Javadoc for your project, and then packages the generated documentation into a zip file

To do this we added the new following task called packageJavadoc:

    task packageJavadoc(type: Zip) {
        group = "Documentation"
        description = "Generates Javadoc and packages it into a ZIP file."
    
        dependsOn javadoc
    
        from javadoc.destinationDir
    
        destinationDirectory = file("$rootDir/app")
    
        archiveFileName = "javadoc.zip"
    
        doLast {
            println "Javadoc has been generated and packaged into $destinationDirectory/javadoc.zip"
        }
    }


- dependsOn javadoc: This makes the packageJavadoc task depend on the javadoc task. Gradle will run the javadoc task first to generate the documentation before executing this task.

- from javadoc.destinationDir: This defines the source directory for the ZIP file as the output of the javadoc task. The javadoc.destinationDir holds the path where the generated Javadoc is stored.

- destinationDirectory = file("$$rootDir/docs"): This specifies where the ZIP file will be stored. In this case, it will be placed in the app folder.

- archiveFileName = "javadoc.zip": This sets the name of the ZIP file that will be created.

![img_1.png](../pictures/img_1.png)

### Step 5 - Create a new source set for integration tests

To start working on the integration tests we had to make sure we had the correct the dependencies:

    testImplementation 'org.springframework.boot:spring-boot-starter-test'

This dependency is a starter for testing Spring Boot applications with libraries including JUnit Jupiter, Hamcrest and Mockito

We also added the following configuration to use JUnit Platform as the test engine.

    test {
        useJUnitPlatform()
    }

We developed an integration test for the Get All Orders endpoint. To prepare for each test, we introduced a setUp method annotated with @BeforeEach,
which ensures a clean test environment.
This method clears all existing records from the database
, then creates and saves two new orders with distinct statuses and descriptions.
By resetting the database and populating it with fresh data, we ensure consistent test
results.

![img_3.png](../pictures/img_3.png)

After that we created a method to perform the Get All Orders and validate that the response was Ok 200.
![img_2.png](../pictures/img_2.png)


## Part 2 - Alternative(Ant)

### Step 1- Convert the building rest services to Ant (instead of Maven)


For this part we decided to use Ivy in combination with Ant
Ivy complements Ant by automating dependency management. With Ivy, we can define the project dependencies in a simple ivy.xml file, and it will automatically download and manage the correct versions of libraries from central repositories (like Maven Central).

In a new directory we created the build.xml file and an ivy.xml file.

In the build.xml file we defined the ivy dependency using the following code:


![img_4.png](../pictures/img_4.png)

The Ivy tasks were loaded, the path to where ivy configurations are located and where the ivy.xml file is located was also configured


In the ivy.xml file we defined the dependencies we needed

![img_5.png](../pictures/img_5.png)


After that we ran the "ant" command, to install all the dependencies needed

In the same build.xml we added the compile and jar targets, to compile and create the jard for the application

![img_6.png](../pictures/img_6.png)

![img_7.png](../pictures/img_7.png)


We ran the ant jar, that depends on the compile task, so it runs both targets

To be able to run the application we added the following target, named runApplication

![img_8.png](../pictures/img_8.png)

To run the application we just need to run the following command

    ant run application


### Step 2 Create a custom task that zips and stores in backup directory

For this step we created 5 tasks that work like steps that depende on each other to clean the backup, create the backup directory, copy the source code to a temporary backup directory and to zip the copied source code

To run this step, we just need to run the zipSourceCode target, that runs all the other ones, that are dependent, with the following command:

    ant zipSourceCode

![img_9.png](../pictures/img_9.png)

### Step 3 -  Create a custom task that depends on the installDist task and runs the application using the generated distribution scripts

Apache Ant employs a simpler, XML-based scripting model and lacks built-in, high-level tasks such as Gradle's installDist.
Tasks like copying JAR files, managing dependencies, and generating platform-specific scripts (e.g., shell scripts for Unix-based systems or batch scripts for Windows) must be manually defined in Ant.

Furthermore, Ant does not include a built-in mechanism for detecting the operating system and adjusting the build process accordingly.
Achieving OS-specific behavior in Ant involves using external properties or conditionals, which introduces additional complexity and verbosity.

### Step 4 Generates javadoc and zips it.

    <path id="project.classpath">
        <pathelement path="${build.dir}/classes"/>
        <fileset dir="${lib.dir}" includes="**/*.jar"/>
    </path>

We started by difining a named path, which can be referenced by other elements.
Specifies a set of files, that includes all .jar files found in the library directory and its subdirectories.


    <property name="javadoc.dir" value="${build.dir}/javadoc"/>

This line defines a property called javadoc.dir, which indicates the directory where the generated Javadoc documentation will be stored.


    <target name="generateJavadoc">
        <mkdir dir="${javadoc.dir}"/>
        <javadoc destdir="${javadoc.dir}" sourcepath="${src.dir}" classpathref="project.classpath">
            <packageset dir="${src.dir}">
                <include name="**/*.java"/>
            </packageset>
        </javadoc>
        <echo message="Javadoc generated in ${javadoc.dir}"/>
    </target>


This defines a new target called generateJavadoc and creates the directory where the Javadoc documentation will be generated if it does not already exist.
Then the task javadoc will generate the javadoc documentation using the src directory.

    <property name="zip.name" value="javadoc.zip"/>

This defines a property called zip.name, which stores the name of the ZIP file that will be created to package the Javadoc documentation.

    <target name="packageJavadoc" depends="generateJavadoc">
        <mkdir dir=""/>
        <zip destfile="${zip.name}">
            <fileset dir="${javadoc.dir}"/>
        </zip>
        <echo message="Javadoc packaged into /${zip.name}"/>
    </target>

This defines another target called packageJavadoc, which depends on the generateJavadoc target.
It creates a ZIP file with the name defined in the zip.name property, using the file set of the javadoc directory.

To run this step, we just need to run the zipSourceCode target, that runs all the other ones, that are dependent, with the following command:

    ant packageJavadoc

### Step 5 - Create a new source set for integration tests Add a simple test and the needed dependencies and tasks to run the test

We added an integration test that starts the application and performs a get to the all orders endpoint, validating the return code ( 200 Ok ) .
Before the test validation, we cleaned the database and added two orders in the database. 

![img_10.png](../pictures/img_10.png)


### Step 2- Adding Unit Tests and Configuring Ant for JUnit

Next, we set up unit testing using JUnit in Ant. For this, we created a target named test
which compiles the test classes and runs them using the junit task:


    <property name="test.classes.dir" value="${build.dir}/test-classes"/> 

    <target name="test" depends="compileTests">
        <mkdir dir="${build.dir}/test-reports"/>
        <junit printsummary="on" haltonfailure="yes" haltonerror="yes" fork="true" showoutput="true">
            <classpath>
                <pathelement path="${classes.dir}"/>
                <pathelement path="${test.classes.dir}"/>
                <fileset dir="${lib.dir}" includes="**/*.jar"/>
            </classpath>
            <formatter type="plain"/>
            <batchtest todir="${build.dir}/test-reports">
                <fileset dir="${test.classes.dir}">
                    <include name="**/*Test.class"/>
                </fileset>
            </batchtest>
        </junit>
    </target>

First we defined one property with the test classes directory.
We added the class path with the all the java classes, tests and libs that we needed.

- Test Logging: The test results are printed, and the task halts on failure or error, providing a quick summary, just like in Gradle with testLogging.
- Batch Test: It scans the compiled test classes (*Test.class) and runs all of them.

To run the unit tests, we used:

    ant test