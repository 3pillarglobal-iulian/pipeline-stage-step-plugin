HOW TO INSTALL:

1. Compile and install the plugin using the command 'mvn clean install', but make sure you are in the plugin's directory.
2. Go in in the ${basedir}/target directory and copy the file 'pipeline-stage-step.hpi' to ${JENKINS_HOME}/plugins (where $JENKINS_HOME is the system environment variable set by Jenkins).


HOW TO USE:
 In order to create checkpoints and skip stages do the following:
 
1. Add "checkpoint STAGE_NAME" at the end of every stage you want to skip (or in a post-success section).
2. Save your pipeline and build the job (If the checkpoint was created, you will see the following message in the Console Output: "Checkpoint made in: {checkpointFolderPath}").
3. Use replay action on your build to skip the checkpointed stages.

