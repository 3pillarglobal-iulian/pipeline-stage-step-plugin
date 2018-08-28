HOW TO INSTALL:

1. Compile and install the plugin using the command 'mvn clean install', but make sure you are in the plugin's directory.
2. Go in in the ${basedir}/target directory and copy the file 'pipeline-stage-step.hpi' to ${JENKINS_HOME}/plugins (where $JENKINS_HOME is the system environment variable set by Jenkins).


HOW TO USE:

I) In order to create checkpoints and skip stages do the following:
1. Add "checkpoint STAGE_NAME" at the end of every stage you want to skip (or in a post-success section).
2. In the same 'Configure' page: check 'This project is parameterized' -> Add Parameter -> String Parameter. In the 'Name' field write "buildWithCheckpoint".
3. Save your pipeline and build the job by pressing 'Build with Parameters'. If the checkpoint was created, you will see the following message in the Console Output: "Checkpoint made in: {checkpointFolderPath}".
4. When you build your job again, the stages with created checkpoints in the previous build should be now skipped. (You can check in Console Output if "Skipping stage Build" appears).

II) In order to build without checkpoints do the following:
1. If you have a parameter named "buildWithCheckpoint" remove it.
2. (Optional) Remove 'checkpoint STAGE_NAME' step from your code in order to delete older checkpoints.
3. Build the job normally.
