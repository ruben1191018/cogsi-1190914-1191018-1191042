sudo apt-get update -y
sudo apt-get upgrade -y

sudo apt-get install -y wget unzip

# Install OpenJDK (Java 17)
sudo apt-get install -y openjdk-17-jdk

# Set JAVA_HOME system-wide
sudo bash -c 'echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" > /etc/profile.d/jdk.sh'
sudo bash -c 'echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> /etc/profile.d/jdk.sh'

wget https://github.com/h2database/h2database/releases/download/version-2.3.230/h2-2024-07-15.zip -P /tmp
sudo unzip -d /home/vagrant/ /tmp/h2-2024-07-15.zip

# Create a script to run the H2 server with disk storage
sudo cat <<EOF > start_h2.sh
#!/bin/bash
java -cp /home/vagrant/h2/bin/h2*.jar org.h2.tools.Server -tcp -tcpAllowOthers -baseDir /home/vagrant/h2data
EOF


# Make the script executable
chmod +x start_h2.sh


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

# Reload systemd to recognize the new service, enable it, and start it
sudo systemctl daemon-reload
sudo systemctl enable h2
sudo systemctl start h2

sleep 5s

sudo systemctl status h2


# Verify installations
source /etc/profile.d/jdk.sh  # Make sure JAVA_HOME and PATH are available in the current shell

echo "Java version: $(java -version)"

echo "Provisioning completed successfully."