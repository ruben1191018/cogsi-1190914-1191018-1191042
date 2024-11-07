# CA4

## Use Ansible as a provisioner

To start this part of the assignment, we had to define in the VagrantFile this provisioning:

![img.png](img.png)

![img_1.png](img_1.png)

And remove this lines:

![img_3.png](img_3.png)

    app.vm.provision "shell", path: "requirements.sh"
    app.vm.provision "shell", path: "configure_spring.sh"

With this new configuration in the vagrantFile, now we are not using a shell script, but instead we are using ansible and a playbook to help us fulfill this assignment.
This changes were for the app VM and for the DB VM

Now showing the app_playbook that we created: 
![img_2.png](img_2.png)

As you can see in this playbook, it is able to automate the deployment and 
execution of a Spring REST service on a specified host. It creates an 
application directory, clones the necessary Git repository, builds the 
service using Gradle, and then starts it in the background.

Now showing the db_playbook:


    - hosts: db

    become: true
    tasks:
        - name: Update apt package manager
          apt:
            update_cache: yes
    
        - name: Upgrade all packages
          apt:
            upgrade: dist
            update_cache: yes
    
        - name: Create application directory
          become: true
          block:
            - name: Create directory
              file: 
                path: /opt/h2
                state: directory
                mode: '0770'
                group: developers
          rescue:
            - name: Log directory creation failure
              debug:
                msg: "Failed to create /opt/h2 with group developers."


      

        - name: Install required packages
          apt:
            name:
              - wget
              - unzip
            state: present
    
        - name: Check if Java is installed
          command: java -version
          register: java_installed
          ignore_errors: yes
    
        - name: Install OpenJDK 17
          apt:
            name: openjdk-17-jdk
            state: present
          when: java_installed.rc != 0
    
        - name: Set JAVA_HOME in system-wide profile
          copy:
            dest: /etc/profile.d/jdk.sh
            content: |
              export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-$(uname -m)
              export PATH=$JAVA_HOME/bin:$PATH
    
        - name: Set permissions on JAVA_HOME profile script
          file:
            path: /etc/profile.d/jdk.sh
            mode: "0755"
    
        - name: Source JAVA_HOME and PATH for the current session
          shell: sh /etc/profile.d/jdk.sh
    
        - name: Download H2 Database zip file
          get_url:
            url: https://github.com/h2database/h2database/releases/download/version-2.3.230/h2-2024-07-15.zip
            dest: /tmp/h2-2024-07-15.zip
    
        - name: Unzip H2 Database into /home/vagrant/
          unarchive:
            src: /tmp/h2-2024-07-15.zip
            dest: /home/vagrant/
            remote_src: yes
    
        - name: Create H2 startup script
          copy:
            dest: /home/vagrant/start_h2.sh
            content: |
              #!/bin/bash
              java -cp /home/vagrant/h2/bin/h2*.jar org.h2.tools.Server -tcp -tcpAllowOthers -baseDir /opt/h2
    
        - name: Set permissions on H2 startup script
          file:
            path: /home/vagrant/start_h2.sh
            mode: "0755"
    
    
        - name: Create systemd service file for H2
          copy:
            dest: /etc/systemd/system/h2.service
            content: |
              [Unit]
              Description=H2 Database Service
              After=network.target
    
              [Service]
              ExecStart=/home/vagrant/start_h2.sh
              User=vagrant
              Restart=always
    
              [Install]
              WantedBy=multi-user.target
    
        - name: Set permissions on H2 startup script
          file:
            path: /etc/systemd/system/h2.service
            mode: "0755"
    
        - name: Reload systemd to recognize the new service
          command: sudo systemctl daemon-reload
    
        - name: Enable H2 service
          systemd:
            name: h2
            enabled: true
            state: started
    
        - name: Check H2 service status
          command: sudo systemctl status h2
          register: h2_status
          ignore_errors: yes
          failed_when: "'active (running)' not in h2_status.stdout"
    
        - name: Print H2 service status
          debug:
            var: h2_status.stdout


The main purpose of this playbook is to set up an H2 database on a host 
by updating packages, installing Java, and configuring H2. It creates 
necessary directories and scripts, sets up a systemd service for H2, and 
starts the service, ensuring it runs continuously.

## Use Ansible to configure PAM to enforce a complex password policy

In this task, our goal is to automate the process of enforcing a strong password policy for all users on the system using Ansible. By leveraging Ansible’s automation capabilities, we can ensure consistent application of security standards across all machines, reducing the risk of weak passwords and enhancing overall security.


To start, we configured the Vagrantfile to use Ansible for provisioning both virtual machines. The following configuration snippet enables Vagrant to run an Ansible playbook, common_playbook.yml, on both VMs. This setup ensures that the same password policies are applied across all instances automatically:

![img_4.png](img_4.png)

By specifying compatibility_mode as “2.0,” we maintain compatibility with Ansible's newer syntax and playbook requirements.

The playbook, common_playbook.yml, is designed to apply a complex password policy 
by configuring PAM (Pluggable Authentication Modules). PAM is a common framework for 
authentication in Linux systems, allowing us to enforce various password standards. 
This playbook applies to all hosts defined in the Ansible inventory and runs with 
elevated privileges (via become: true), as system-level configurations require root access.


      - name: Deploy and run Spring REST service on host1
        hosts: all
        become: true
        tasks:
            - name: Install libpam-pwquality
              package:
              name: "libpam-pwquality"
              state: present
            
            - name: Configure pam_pwquality
              lineinfile:
              path: "/etc/pam.d/common-password"
              regexp: "pam_pwquality.so"
              line: "password required pam_pwquality.so minlen=12 lcredit=-1 ucredit=-1 dcredit=-1 ocredit=-1 retry=3 enforce_for_root"
              state: present  

The first task(Install libpam-pwquality) in the playbook installs the libpam-pwquality
package, which is essential for enforcing password complexity rules in PAM. Without this
package, PAM lacks the functionality to impose specific rules on passwords, such as 
character requirements or minimum length.

The second task(Configure pam_pwquality) uses the lineinfile module to configure the 
password policy in the PAM configuration file located at /etc/pam.d/common-password. 
This configuration file manages password requirements for PAM, making it the appropriate
place to specify our password complexity rules.
Besides that, the second task does the following:

1. Checks for Existing Configuration: The regexp option searches for a line containing "pam_pwquality.so" within the configuration file. If found, Ansible replaces it with the new line defined in line.

2. Defines Complexity Rules: The line enforces specific password rules:
- minlen=12: Requires a minimum password length of 12 characters.
- lcredit=-1: Requires at least one lowercase letter.
- ucredit=-1: Requires at least one uppercase letter.
- dcredit=-1: Requires at least one digit.
- ocredit=-1: Requires at least one special character.
- retry=3: Allows users up to three attempts if they enter an invalid password.
- enforce_for_root: Enforces these rules for the root user as well, ensuring security standards are uniformly applied.

Each of these rules enhances password security by making it more difficult to 
guess or brute-force user passwords.

## Use Ansible to create a group called developers and a user devuser on the two VMs









