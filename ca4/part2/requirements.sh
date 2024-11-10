#!/bin/bash

# Update package list and upgrade all packages
sudo apt-get update -y
sudo apt-get upgrade -y

# Install Git
sudo apt-get install -y git

# Install OpenJDK (Java 11)
sudo apt-get install -y openjdk-17-jdk

# Set JAVA_HOME system-wide
sudo bash -c 'echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" > /etc/profile.d/jdk.sh'
sudo bash -c 'echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> /etc/profile.d/jdk.sh'

# Install Maven
sudo apt-get install -y maven

# Install Gradle (Latest version)
sudo apt-get install -y wget unzip
wget https://services.gradle.org/distributions/gradle-8.4-bin.zip -P /tmp
sudo unzip -d /opt/gradle /tmp/gradle-8.4-bin.zip
sudo ln -s /opt/gradle/gradle-8.4/bin/gradle /usr/bin/gradle

# Verify installations
source /etc/profile.d/jdk.sh  # Make sure JAVA_HOME and PATH are available in the current shell

echo "Git version: $(git --version)"
echo "Java version: $(java -version)"
echo "Maven version: $(mvn -version)"
echo "Gradle version: $(gradle -v)"

echo "Provisioning completed successfully."
