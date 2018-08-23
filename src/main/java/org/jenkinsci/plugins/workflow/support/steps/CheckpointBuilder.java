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
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

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
        boolean dirCreated = false;

        if(!dir.exists()){
            dirCreated = dir.mkdir();
        }

        if (!dirCreated && !dir.exists()) {
            return;
        }

        Optional<File[]> arrayOfFiles = Optional.ofNullable(dir.listFiles());

        boolean fileExist = false;

        if (arrayOfFiles.isPresent()) {
            fileExist = Arrays.stream(arrayOfFiles.get()).anyMatch(file -> file.getName().equals(stageName));
        }

        if (!fileExist) {
            File newFile = new File(dirPath + "/" + stageName);
            try {
              boolean fileCreated = newFile.createNewFile();
              if (!fileCreated) {
                  System.out.println("Couldn't create file");
              }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        EnvVars envVars = run.getEnvironment(taskListener);
        String jobName = envVars.get("JOB_BASE_NAME");

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
