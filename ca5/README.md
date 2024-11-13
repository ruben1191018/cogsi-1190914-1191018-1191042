


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

    docker build -f Dockerfile_version2 -t rest-app-image-version2 .

In this command, we specified the docker file name and tag the image with the name rest-app-image-version2.

![alt text](image-1.png)

To run the application:

    docker run -p 8080:8080 rest-app-image-version2  


The docker run command starts a new container from the rest-app-image-version2 Docker image.
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
This instruction copies the JAR file from the build/libs/basic_demo-0.1.0.jar directory on the host machine into the /app directory in the container and renames it to my-server.jar. This is necessary to include the application code that will be executed when the container runs.

* ENTRYPOINT ["java", "-cp", "/app/my-server.jar", "basic_demo.ChatServerApp", "59001"]:
This sets the command that is executed when the container starts. Here, java is run with the -cp (classpath) option, which specifies my-server.jar as the classpath. The main class basic_demo.ChatServerApp is the entry point for the application, and 59001 is an argument passed to it (the port the server listens on).


After creating this Dockerfile, we built the image using:

    docker build -f Dockerfile_version2 -t chat-app-image-version2 .

In this command, we specified the docker file name and tag the image with the chat-app-image-version2.

![alt text](image-4.png)

To run the application:

    docker run -p 59001:59001 chat-app-image-version2


The docker run command starts a new container from the chat-app-image-version2 Docker image.
The -p 59001:59001 flag ensures that any requests to localhost:8080 on the host are forwarded to port 8080 in the container where the Java application is listening.

After this, we tested that the application was running:
![alt text](image-5.png)


### Display the history of each image, showing each layer and command used to create the image Version 2

To display the history of each image we ran:

    docker history rest-app-image-version2

![alt text](image-6.png)

    docker history chat-app-image-version2

![alt text](image-7.png)


### Monitor container resource consumption in real-time Version 2

In order to monitor both containers we ran the docker status command where we can see the following properties:

* CPU %: Percentage of CPU usage.
* MEM USAGE / LIMIT: Amount of memory used vs. total available memory for the container.
* MEM %: Percentage of memory used relative to the available memory.
* NET I/O: Network I/O stats (data sent/received).
* BLOCK I/O: Block I/O stats (disk read/write).
* PIDS: Number of processes running inside the container.

So we ran 

    docker stats REST_APP_V2

![alt text](image-8.png)

and 

    docker stats CHAT_APP_V2

![alt text](image-9.png)

### You should tag your images and publish them in Docker Hub version 2

To deploy Docker images to Docker Hub, we followed a sequence of steps that involved logging in, tagging the images, and pushing them to the repository. Below is a detailed breakdown of each step and its purpose.

#### Log in to Docker Hub
First, we authenticated to Docker Hub by running the following command:

    docker login

This command prompts the user to enter their Docker Hub credentials (username and password) to establish a session with Docker Hub. Logging in is a necessary step to gain authorization for pushing images to our Docker Hub repositories.

#### Tagging the Images

Next, we assigned tags to each image. Tagging associates each local image with a specific repository and tag format that Docker Hub recognizes. We used the following commands:

    docker tag rest-app-image-version2 1191018/cogsi-rest-v2:latest
    docker tag chat-app-image-version2 1191018/cogsi-chat-v2:latest

* docker tag: This command is used to label an existing local Docker image with a new name and tag.
* rest-app-image-version2 and chat-app-image-version2: These are the original names of the local images we created or built.
* 1191018/cogsi-rest-v2:latest and 1191018/cogsi-chat-v2:latest: These are the new names and tags assigned to each image. The format here is username/repository:tag.
    * 1191018: Represents our Docker Hub username.
    * cogsi-rest-v2 and cogsi-chat-v2: Specify the unique repository names for each image.
    * latest: The chosen tag for this version of each image. The latest tag is commonly used to represent the most recent stable version.


#### Pushing the Images to Docker Hub
Finally, we pushed each tagged image to Docker Hub using the following commands:

    docker push 1191018/cogsi-rest-v2:latest
    docker push 1191018/cogsi-chat-v2:latest

* docker push: This command uploads the tagged images from the local system to the specified Docker Hub repositories.

* 1191018/cogsi-rest-v2:latest and 1191018/cogsi-chat-v2:latest: These refer to the fully qualified names of each image on Docker Hub, as specified during the tagging step.

Once pushed, the images are available in our Docker Hub account under the specified repositories and can be pulled and used by others if permissions allow.

This structured approach ensures that each image is correctly tagged and stored in a centralized Docker Hub repository, making it accessible for deployment and collaboration.

