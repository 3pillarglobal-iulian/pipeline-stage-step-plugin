HOW TO INSTALL:

1. First, you need to compile the plugin using the command 'mvn clean install'.
2. Go in project folder/target and copy the file 'pipeline-stage-step.hpi' to "$JENKINS_HOME/plugins" (where $JENKINS_HOME is the system environment variable set by Jenkins).


HOW TO USE:

I) In order to start a build with skipped checkpoint do the following:
1. Add "checkpoint STAGE_NAME" as a last step in every stage you want to skip (or in a post-success).
2. Build your job, and if in the Console Output the following line appears 'Checkpoint made in: {checkpointFolderPath}', if not, something went wrong.
3. Go to Configure > Check 'This project is parameterized', and add a String parameter with the exact name "buildWithCheckpoint" (the default value doesn't matter).
4. Now in the job page, build your job again by using Build with Parameters.
5. The stage with checkpoints from the previous build should be now skipped. (You can check in Console Output if "Skipping stage Build" appears)

II) In order to build without checkpoints do the following:
1. If you have a parameter named "buildWithCheckpoint" remove it.
1.2. (Optional) Remove 'checkpoint STAGE_NAME' step calls from your code in order to delete older checkpoints.
2. Build the job normally.


