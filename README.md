# Risk-SC
Fall 2018 Final Project for CSCI 201 at USC. This project will simulate the board game Risk with modifications relating to USC. 

## Instructions for Contributors

### Set up SSH Key (Only for Mac)
1. Navigate to an appropriate directory
2. Run `curl -O https://raw.githubusercontent.com/NilayPachauri/Scripts/master/Useful/git_config`
3. Open up the file `git_config` in any text editor
4. Edit the `NAME` and `EMAIL` parameters at the top to the appropriate values and save the file
    - `NAME` is your personal full name. You may uses spaces within the quotes.
    - `EMAIL` is the email associated with your GitHub account
5. Run `chmod a+x git_config`
6. Run `./git_config`
    - There is no need to enter anything. Just keep hitting enter till the key pops up.
7. Copy the generated key starting with `ssh` and ending with your email
8. Navigate to your GitHub account online
9. Click `Settings->SSH and GPG Keys` from the top right of the screen
10. Click the green button `New SSH Key`
11. In the `key` field, paste the key copied in step 7
12. Int the `title` field, name it something meaningful
13. Click `Add SSH Key`

### Clone the Repository
1. Navigate to the directory of the workspace you will be using for Eclipse
    - Make sure you have Tomcat set up in that server in accordance with Lab1 of CSCI 201
2. Run `git clone git@github.com:NilayPachauri/Risk-SC.git` from the command line

### Importing Repository into Eclipse
1. Go to `File->Import->General->Existing Projects into Workspace`
2. Select `Select Root Directory` and choose the directory you cloned the repository to in the Clone the Repository Phase
3. Make sure `Risk-SC` is checked under `Projects` and only `Search for nested projects` is checked under `Options`
4. Click `Finish`
5. Open up the newly created Project in your Workbench in Eclipse and make changes
