package org.jenkinsci.plugins.workflow.support.steps;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;

import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

public class CheckpointBuilder extends Builder implements SimpleBuildStep {

    private final String stageName;

    @DataBoundConstructor
    public CheckpointBuilder(String stageName) {
        this.stageName = stageName;
    }

    public String getStageName() {
        return stageName;
    }

    private void createStageNameFile(String jobName) {
        String dirPath = System.getenv("JENKINS_HOME") + "/workspace/" + jobName + "@checkpoint";
        File dir = new File(dirPath);

        if(!dir.exists()){
            dir.mkdir();
        }

        File folder = new File(dirPath);
        File[] listOfFiles = folder.listFiles();

        boolean fileExist = false;

        for (File file : listOfFiles) {
            if (file.getName().equals(stageName)) {
                fileExist = true;
            }
        }

        if (!fileExist) {
            File newFile = new File(dirPath + "/" + stageName);
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        EnvVars envVars = run.getEnvironment(taskListener);
        String jobName = envVars.get("JOB_BASE_NAME");
        Executor executor = run.getExecutor();

        taskListener.getLogger().println(System.getenv("JENKINS_HOME") + "/workspace/" + jobName + "@checkpoint");
        taskListener.getLogger().println("Checkpoint plugin");

        createStageNameFile(jobName);
    }

    @Symbol("checkpoint")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "checkpoint";
        }

    }
}
