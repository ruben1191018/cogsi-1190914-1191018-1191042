


## Version 2: Build the server on your host machine and copy the resulting JAR file into the Docker image


### Building Rest Version 2

First, we ran the gradle build command to compile the project and create the JAR file.
After that, we verified that the JAR was generated.
![alt text](image.png)

Next, we created a custom Dockerfile, named "Dockerfile_version2", with the following configurations:

    FROM openjdk:17-jdk-slim

    WORKDIR /app

    COPY app/build/libs/app.jar /app/my-server.jar

    ENTRYPOINT ["java", "-jar", "/app/my-server.jar"]


* FROM openjdk:17-jdk-slim:
This line specifies the base image for the container. We use openjdk:17-jdk-slim, a lightweight Java Development Kit image that includes the minimum necessary packages to run Java applications. This keeps the container size small and efficient.

* WORKDIR /app:
    This sets the working directory inside the container to /app. All subsequent COPY, RUN, CMD, and other instructions will use this directory as their base path, making it easy to manage files and operations within the container.

* COPY app/build/libs/app.jar /app/my-server.jar:
This instruction copies the JAR file (app.jar) from the app/build/libs/ directory on the host machine into the /app directory in the container and renames it to my-server.jar. This is necessary to include the application code that will be executed when the container runs.

* ENTRYPOINT ["java", "-jar", "/app/my-server.jar"]:
 This sets the command that the container runs when it starts. The ENTRYPOINT instruction runs the Java runtime and executes the my-server.jar file, launching the application. Using ENTRYPOINT ensures the specified command is always executed, making the container behavior predictable.


After creating this Dockerfile, we built the image using:

    docker build -f Dockerfile_version2 -t my-server-image .

In this command, we specified the docker file name and tag the image with the name my-server-image.

![alt text](image-1.png)

To run the application:

    docker run -p 8080:8080 my-server-image  


The docker run command starts a new container from the my-server-image Docker image.
The -p 8080:8080 flag ensures that any requests to localhost:8080 on the host are forwarded to port 8080 in the container where the Java application is listening.

After this, we tested that the application was running:
![alt text](image-2.png)

### Building Chat Version 2

First, we ran the gradle build command to compile the project and create the JAR file.
After that, we verified that the JAR was generated.
![alt text](image-3.png)

Next, we created a custom Dockerfile, named "Dockerfile_version2", with the following configurations:

    FROM openjdk:17-jdk-slim

    WORKDIR /app

    COPY build/libs/basic_demo-0.1.0.jar /app/my-server.jar

    ENTRYPOINT ["java", "-cp", "/app/my-server.jar", "basic_demo.ChatServerApp", "59001"]




* FROM openjdk:17-jdk-slim:
This line specifies the base image for the container. We use openjdk:17-jdk-slim, a lightweight Java Development Kit image that includes the minimum necessary packages to run Java applications. This keeps the container size small and efficient.

* WORKDIR /app:
    This sets the working directory inside the container to /app. All subsequent COPY, RUN, CMD, and other instructions will use this directory as their base path, making it easy to manage files and operations within the container.

* COPY build/libs/basic_demo-0.1.0.jar /app/my-server.jar
This instruction copies the JAR file (app.jar) from the build/libs/basic_demo-0.1.0.jar directory on the host machine into the /app directory in the container and renames it to my-server.jar. This is necessary to include the application code that will be executed when the container runs.

* ENTRYPOINT ["java", "-cp", "/app/my-server.jar", "basic_demo.ChatServerApp", "59001"]:
This sets the command that is executed when the container starts. Here, java is run with the -cp (classpath) option, which specifies my-server.jar as the classpath. The main class basic_demo.ChatServerApp is the entry point for the application, and 59001 is an argument passed to it (the port the server listens on).


After creating this Dockerfile, we built the image using:

    docker build -f Dockerfile_version2 -t my-server-image .

In this command, we specified the docker file name and tag the image with the name my-server-image.

![alt text](image-4.png)

To run the application:

    docker run -p 59001:59001 my-server-image  


The docker run command starts a new container from the my-server-image Docker image.
The -p 59001:59001 flag ensures that any requests to localhost:8080 on the host are forwarded to port 8080 in the container where the Java application is listening.

After this, we tested that the application was running:
![alt text](image-5.png)
