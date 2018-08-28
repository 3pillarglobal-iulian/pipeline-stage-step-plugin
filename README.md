HOW TO INSTALL:

1. Compile and install the plugin using the command 'mvn clean install', but make sure you are in the plugin's directory.
2. Go in in the ${basedir}/target directory and copy the file 'pipeline-stage-step.hpi' to ${JENKINS_HOME}/plugins (where $JENKINS_HOME is the system environment variable set by Jenkins).


HOW TO USE:

I) In order to start a build with skipped checkpoint do the following:
1. Add "checkpoint STAGE_NAME" at the end of every stage you want to skip (or in a post-success section).
2. Build your job. If the checkpoint was created, you will see the following message in the Console Output: "Checkpoint made in: {checkpointFolderPath}".
3. Go to Configure -> Check 'This project is parameterized' -> Add Parameter -> String Parameter. In the 'Name' field write "buildWithCheckpoint".
4. Save the changes and build your job again by pressing 'Build with Parameters'.
5. The stage with checkpoints from the previous build should be now skipped. (You can check in Console Output if "Skipping stage Build" appears).

II) In order to build without checkpoints do the following:
1. If you have a parameter named "buildWithCheckpoint" remove it.
2. (Optional) Remove 'checkpoint STAGE_NAME' step from your code in order to delete older checkpoints.
3. Build the job normally.


