# CA1 Report

## Part 1 ( No branches )

### Step 1: Setting Up the Project
Firstly we cloned the project with the following command
    
    git clone https://github.com/ruben1191018/cogsi-1190914-1191018-1191042.git

We began by copying the "Building REST Services with Spring" application into a new 
directory (ca1). After that, we created the first issue to track progress.

![img.png](imgs/img.png)

### Step 2: Initial Commit
With the issue in place, we proceeded with the initial commit. We used a smart commit referring to the issue created before. The steps were as follows:

- git pull – to ensure the latest version of the repository is up-to-date.
- git add . – to stage the newly added files. 
- git commit -m "#1 Add the tut-rest project" – to commit the changes.
- git push – to push the commit to the repository.

### Step 3: Adding .gitignore
After this, we created Issue 2, which involved adding a .gitignore file to the project:

![img2.png](imgs/img2.png)

- git pull – to fetch the latest changes.
- git add .gitignore – to stage the .gitignore file.
- git commit -m "Add git ignore #2" 
- git push – to push the commit.

### Step 4: Tag Creation

After completing the initial setup and configuration, we created and pushed the tag 1.1.0 for the project with the following commands

    git tag -a 1.1.0 3d068d9
    git push origin 1.1.0


### Step 5: Project Enhancements
Subsequently, a change was made to the project links by adding two new fields to the Employee model. The necessary support for these new fields was also implemented.

Commit - https://github.com/ruben1191018/cogsi-1190914-1191018-1191042/commit/3cbd9a96165e61629aad6a4fa2c62b6253c35490

![img3.png](imgs/img3.png)

- git pull – to fetch the latest changes.
- git add . – to stage all the changes
- git commit -m "#3 Add new fields to the code"
- git push – to push the changes.


After the code was implemented we added the tests
- git add . – to stage all the changes
- git commit -m "#5 Add Employee tests"
- git push – to push the changes.

And we created and pushed the new tag with the new minor version

    git tag 1.2.0
    git push origin 1.2.0

### Step 6: Git Log

After all the changes we used different git log commands to view the commit history:

To show the information in a compact way

- git log --oneline- 

![img.png](imgs/gitlog1.png)

To show the commits in graph form

     git log --graph --oneline 

![img_1.png](imgs/gitlog2.png)

To show the commit in a formatter way with the id of the commit, name of who commited, time it was done and the name of the commit

    git log --pretty=format:"%h - %an, %ar : %s"

![img_2.png](imgs/gitlog3.png)

To test the revert command

### Step 7: Git Revert

To test the revert command we created a commit (only adding on new line in a file) so we could revert to it

![img.png](img.png)

After that we reverted to that commit with the following command

    git revert 5f3040778133cc7ca2e7e715bb30e8c6aeafda73

![img_1.png](imgs/git_revert.png)

### Step 8: Add final git tag for part 1

    git tag ca1-part1
    git push origin ca1-part1

## Part 2- Branches

### Step 1: Create branch named email-field

To create the branch we used the following command:

    git checkout -b email-field

### Step 2: Add support for an email field for and employee 

After that we implemented the new email field in the class employee

Commit- https://github.com/ruben1191018/cogsi-1190914-1191018-1191042/commit/ea946816cb221b029499abe1d377e461ccbc801a

![img_1.png](imgs/new_email_field.png)

    git commit -m "#6 Add new email field"

### Step 3: Add unit tests
Commit - https://github.com/ruben1191018/cogsi-1190914-1191018-1191042/commit/a43664bce078cfd8269578ff24f8dbc53c4c41f1

![img_1.png](imgs/unit_tests_email.png)

    git commit -m "#7 Add test for null and empty email"

### Step 4: Merge branch into main

After the changes and tests were implemented we merged the branch in main with the following commands:

Checkout to main:
 
    git checkout main

Merge the email-field branch
    
    git merge email-field

Push the changes to the remote repository 

    git push

### Step 5: Add new tag 1.3.0

    git tag 1.3.0
    git push 1.3.0


### Step 6: Create branch named fixing-invalid-email

To create the branch we used the following command:

    git checkout -b fixing-invalid-email

### Step 7: Add email validation for At Sign (@)

After that we implemented the email validation in the class employee

Commit- https://github.com/ruben1191018/cogsi-1190914-1191018-1191042/commit/865be3c3f10ab72439a6643dc12d8a94ff77d07c

![img_1.png](imgs/email_@_validation.png)

    git commit -m "#8 Add email validation Update Employee.java"

### Step 8: Add unit tests
Commit - https://github.com/ruben1191018/cogsi-1190914-1191018-1191042/commit/27ec0f38827f8763fb66b3d50f54c493ab7be605

![img_1.png](imgs/email_@_test.png)

    git commit -m "#9 Add tests for email sign validation"

### Step 9: Merge branch into main

After the changes and tests were implemented we merged the branch in main with the following commands:

Checkout to main:

    git checkout main

Merge the fixing-invalid-email branch

    git merge fixing-invalid-email

Push the changes to the remote repository

    git push

### Step 10: Add new tag 1.3.1

    git tag 1.3.1
    git push 1.3.1

