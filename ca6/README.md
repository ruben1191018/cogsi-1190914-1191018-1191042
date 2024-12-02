# Part1

In this assignment, we developed an end-to-end solution to automate the deployment process for a Gradle-based 
Spring application.

## Setting Up the Infrastructure

### Vagrant Virtual Machines

To isolate our deployment environments, we created two virtual machines (VMs) named blue and green using Vagrant.

We created a Vagrantfile to automate the creation of the VMs:

    Vagrant.configure("2") do |config|

        # Custom DNS and hostname for better identification
        config.dns.tld = "mydomain"
        config.vm.hostname = "mymachine"
    
        # Using Ubuntu 20.04 as the base OS
        config.vm.box = "bento/ubuntu-20.04"
    
        # Syncing folders for H2 database persistence
        config.vm.synced_folder "./springApplication/h2data", "/home/vagrant/h2data"
    
        # Provisioning the VM with Ansible playbooks
        config.vm.provision "ansible" do |ansible|
            ansible.playbook = "./db_playbook.yml"
            ansible.compatibility_mode = "2.0"
        end
    
        config.vm.provision "ansible" do |ansible|
            ansible.playbook = "./requirements_app_playbook.yml"
            ansible.compatibility_mode = "2.0"
        end
    
        config.vm.provision "ansible" do |ansible|
            ansible.playbook = "./app_playbook.yml"
            ansible.compatibility_mode = "2.0"
        end
    
        # Port forwarding to access the Spring application
        config.vm.network "forwarded_port", guest: 8080, host: 1010
    end


### Ansible for Automation

We used Ansible to automate provisioning and deploying our application. Three playbooks were created:

- Requirements Setup (requirements_app_playbook.yml):
  - This playbook ensured the VMs were prepped with Java, Gradle, and any other dependencies.

- Database Setup (db_playbook.yml):
  - Configures the H2 database, ensuring it is ready to connect to the Spring application.

- Application Deployment (app_playbook.yml):
  - Handles deploying the JAR file, starting the application, and ensuring it runs properly on the blue or green VMs.


## Developing the Jenkins Pipeline

We set up a Jenkins pipeline to handle continuous integration and deployment. 
This required configuring Jenkins on our host machine and creating a Jenkinsfile to define the pipeline logic.

1. Plugins Installed:

   - Suggested Plugins

2. Allowed Script Execution:

   - Enabled shell execution permissions for the gradlew script by running:


        chmod +x gradlew


3. Multibranch Pipeline:

   - We set up a Multibranch Pipeline job to pull code from our repository. This allowed Jenkins to dynamically fetch the latest changes whenever a commit was pushed.


### Pipeline Stages

In our Jenkinsfile, we defined several stages to build, test and deploy the application:


#### Checkout

This stage pulls the latest code from the Git repository:

    stage('Checkout') {
        steps {
            echo 'Pulling the latest source code from the development branch'
            checkout scm
        }
    }

- Uses Jenkins’ built-in checkout scm to pull the latest version of the code from the repository configured in the pipeline job.
- Ensures that every pipeline execution works with the most up-to-date code.


#### Assemble

We compiled the Spring application and generated the JAR file:

    stage('Assemble') {
        steps {
            dir('ca6/springApplication') {
                sh './gradlew clean build -x test'
            }
        }
    }

- Navigates into the project directory ca6/springApplication.
- Runs the Gradle wrapper script gradlew to:
  - Clean the previous build (clean).
  - Build the project and generate a .jar artifact (build).
  - Exclude tests from this step (-x test).

#### Test

To ensure code quality, we ran unit tests:

    stage('Test') {
        steps {
            dir('ca6/springApplication') {
                echo 'Running unit tests to verify the application’s correctness'
                sh './gradlew test'

                echo 'Publishing test results in Jenkins'
                junit '**/build/test-results/test/*.xml'
            }
        }
    }

- Executes all unit tests defined in the project using gradlew test.
- Publishes the test results to Jenkins using the junit step.

#### Archive

We archived the compiled artifacts in Jenkins for later use and tagged stable builds:


    stage('Archive') {
        steps {
            dir('ca6/springApplication') {
                echo 'Archiving the built artifacts'

                script {
                    def version = "stable-v1.${BUILD_NUMBER}"
                    sh "mv app/build/libs/app.jar app/build/libs/${version}.jar"
                }

                archiveArtifacts artifacts: 'app/build/libs/*.jar', allowEmptyArchive: false
            }
        }
    }

Archives the generated .jar files in Jenkins, making them available for deployment or rollback.

#### Deploy to Production

For production deployment, we added a manual approval step:

    stage('Deploy to Production?') {
        steps {
            script {
                echo 'Requesting manual approval for deployment to production'
                timeout(time: 30, unit: 'MINUTES') {
                    input message: 'Do you approve the deployment to production?', ok: 'Proceed'
                }
            }
        }
    }

Pauses the pipeline and waits for a manual approval from an authorized user.


#### Deploy

This stage deployed the applications using Ansible playbooks:


    stage('Deploy') {
        steps {
            dir('ca6') {
                echo 'Deploying the application using Ansible playbooks targeting Vagrant VM'

                // Using Vagrant inventory for the playbooks
                ansiblePlaybook(
                    playbook: 'requirements_app_playbook.yml',
                    inventory: 'vagrant_inventory'
                )
                ansiblePlaybook(
                    playbook: 'db_playbook.yml',
                    inventory: 'vagrant_inventory'
                )
                ansiblePlaybook(
                    playbook: 'app_playbook.yml',
                    inventory: 'vagrant_inventory'
                )
            }
        }
    }


Vagrant_inventory:
    
    [vagrant]
    192.168.33.10


Executes the app_playbook.yml,db_playbook.yml and db_playbook.yml playbook to deploy the applications on the target VM.


### Post-Actions

- Health Checks:

To ensure the application is running correctly after deployment.

    post {
        success {
            echo 'Pipeline succeeded'
            script {
                echo 'Running deployment verification health checks'

                // Example: Using curl to check application health
                def responseCode = sh(
                    script: "curl -o /dev/null -s -w '%{http_code}' http://192.168.33.10:8080/employees",
                    returnStdout: true
                ).trim()

                echo "Health check response code: ${responseCode}"

                if (responseCode != '200') {
                    error("Health check failed! Application is not healthy.")
            }

            echo 'Deployment verification completed successfully'
        }
    }

Uses the curl command to perform a simple HTTP GET request on the application endpoint (/employees).
Checks if the HTTP status code returned is 200 (OK).
    

- Notifications:

Jenkins printed messages indicating the pipeline’s success or failure.


        post {
            always {
                echo 'Pipeline execution completed'
            }
            success {
                echo 'Pipeline succeeded'
            }
            failure {
                echo 'Pipeline failed'
            }
        }


## Adding Rollback Functionality
To handle deployment failures, we created a rollback mechanism using Ansible. The playbook retrieves the last stable artifact from Jenkins and redeploys it.

### Rollback Playbook

![img_1.png](img/img4.png)

1. Download Stable Artifact:


    - name: Download stable artifact
      uri:
      url: "http://localhost:8080/job/part1-pipeline/lastStableBuild/artifact/app.jar"
      dest: "/tmp/app.jar"
      

2. Stop Current Service:


    - name: Stop current service
      shell: kill -9 $(lsof -t -i:8080)
      

3. Deploy and Validate:


    - name: Start service
      command: java -jar /tmp/app.jar
    - name: Perform health check
      uri:
        url: "http://192.168.33.11:8080/employees"


