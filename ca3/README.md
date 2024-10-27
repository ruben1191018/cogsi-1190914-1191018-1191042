# CA3

## Part 1

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
