# CA3

## Part 1

### You should start by creating a VM using Vagrant

We started by running the following command for initializing a project to use a base image to quickly clone a VM:
    vagrant init bento/ubuntu-20.04

This command generates a vagrant file with all the initial configurations.

To automate the installation of all necessary dependencies for the project, we created a shell script called requirements.sh. This script updates the package list, upgrades existing packages, and installs all required dependencies. The key packages installed are:
    * git
    * openjdk-17-jdk
    * maven
    * gradle

We also set the JAVA_HOME variable.

![alt text](images/requirments.png)

After we built the script, we added the following command to the vagrant file:
    config.vm.provision "shell", path: "requirements.sh"

This setting instructs Vagrant to run the requirements.sh script automatically upon VM creation, ensuring that all dependencies are installed without manual intervention


to validate that the VM setup and dependency installations were successful, we performed a series of commands:

Start the VM - We used the command below to boot up the VM with all configurations and provisions specified in the Vagrantfile.
    vagrant up

Connect to the VM - After the VM was running, we connected to it using SSH with the following command:
    vagrant ssh

Verify Installed Dependencies - Once inside the VM, we ran specific version commands to confirm that each required dependency was correctly installed:   
    git --version
    java -version
    gradle -v
    mvn -version

### Clone your group’s repository inside the VM

After inside the machine, we cloned the repo using the following command:
    git clone https://github.com/ruben1191018/cogsi-1190914-1191018-1191042.git


### Interact with both applications from your host machine

Both Building Rest Services with Spring project and Chat Application need to have the ports open to work
So we need to forward ports to expose the VM's network
 
Building Rest Services with Spring project uses the port  8080 so we had to add the following config to the Vagrant file:

      config.vm.network "forwarded_port", guest: 8080, host: 1010

So if we want to connect, on the host machine, to the app running in the guest machine we now can use the 1010 port

And for the Chat Application we used 1011 port

    config.vm.network "forwarded_port", guest: 59001, host: 1011


### Ensure the H2 database in the VM retains data across restarts


To ensure that the H2 database retains data across VM restarts using Vagrant, we can configure the H2 database to store its data on disk. This will require setting up a synced folder between the VM and the host machine to ensure persistent storage.

    # Specify the path on the host and the path inside the VM
    config.vm.synced_folder "./h2data", "/home/vagrant/h2data"

To allow the guest machine to use the shared folder we need to change the permissions
    
    sudo chmod -R 777 ./h2data

After that, we created a new intall_h2.sh script to download and configure the h2

We started by downloading the zip files and unziping it

    wget https://github.com/h2database/h2database/releases/download/version-2.3.230/h2-2024-07-15.zip -P /tmp
    sudo unzip -d /home/vagrant/ /tmp/h2-2024-07-15.zip

After that we created a script to run the H2 Server with disk storage at the /home/vagrant/h2data folder

    # Create a script to run the H2 server with disk storage
    sudo cat <<EOF > start_h2.sh
    #!/bin/bash
    java -cp /home/vagrant/h2/bin/h2*.jar org.h2.tools.Server -tcp -tcpAllowOthers -baseDir /home/vagrant/h2data
    EOF

We needed to make the script executable
    
    chmod +x start_h2.sh

Then we created systemd service file for H2

    # Create a systemd service file for H2 if it doesn't exist
    cat <<EOF | sudo tee /etc/systemd/system/h2.service
    [Unit]
    Description=H2 Database Service
    After=network.target
    
    [Service]
    ExecStart=/home/vagrant/start_h2.sh
    User=vagrant
    Restart=always
    
    [Install]
    WantedBy=multi-user.target
    EOF

And for the final step:

    # Reload systemd to recognize the new service, enable it, and start it
    sudo systemctl daemon-reload
    sudo systemctl enable h2
    sudo systemctl start h2


### Automate the cloning, building, and starting of applications

To streamline the process of cloning, building, and launching applications, we developed a shell script named services.sh. This script allows us to automate each step required to set up and start essential services within the VM.


First, we added the following configuration to the Vagrantfile to provision services.sh with specific environment variables:

    config.vm.provision "shell" do |s|
        s.env = {
        "CLONE_REPO" => "true",
        "START_REST_SERVICE" => "true",
        "START_CHAT_SERVICE" => "true",
        }
        s.path = "services.sh"
    end

In this configuration, we defined three environment variables to control the behavior of services.sh:

* CLONE_REPO: Determines whether the VM should clone the repository.
* START_REST_SERVICE: Specifies whether the VM should start the REST service.
* START_CHAT_SERVICE: Specifies whether the VM should start the chat service.

These settings provide flexibility, enabling or disabling each action depending on the project’s requirements.

This way the VM will run the services.sh before starting.

The services.sh does the following steps:
    * Clones the repo
    * Builds and Starts the chat service in the background to allow to continue with the script.
    * Builds and Starts the rest service in the background to allow to continue with the script.


![alt text](images/services.png)