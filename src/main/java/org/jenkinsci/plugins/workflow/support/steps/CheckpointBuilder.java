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

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class CheckpointBuilder extends Builder implements SimpleBuildStep {

    private static final Logger LOGGER = Logger.getLogger(CheckpointBuilder.class.getName());
    private StepContext context;
    private final String stageName;

    @DataBoundConstructor
    public CheckpointBuilder(String stageName) {
        this.stageName = stageName;
    }

    public String getStageName() {
        return stageName;
    }

    void createCheckpointFile(String jobName, int buildNumber) {
        String checkpointRootPath = System.getenv("JENKINS_HOME") + "/workspace/" + jobName + "@checkpoint";
        String checkpointBuildDirPath = checkpointRootPath + "/" + buildNumber;

        if (!createDirIfDoesNotExist(checkpointRootPath)) { return;}
        if (!createDirIfDoesNotExist(checkpointBuildDirPath)) { return;}

        if (!checkpointFileExist(checkpointBuildDirPath)) {
            File checkpointFile = new File(checkpointBuildDirPath + "/" + stageName);
            createStageNameFile(checkpointFile);
        }
    }

    private boolean createDirIfDoesNotExist(String dirPath) {
        File dir = new File(dirPath);
        if(!dir.exists() && !dir.mkdir()){
                System.out.println("Error when creating root directory");
                return false;
        }
        return true;
    }

    private boolean checkpointFileExist(String path) {
        Path source = Paths.get(path);
        Stream<Path> stream = Stream.empty();
        try {
            stream = Files.walk(source);
            return stream.filter(Files::isRegularFile).anyMatch(file -> file.getFileName().toString().equals(stageName));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
        return false;
    }

    private void createStageNameFile(File checkpointFile) {
        try {
            boolean fileCreated = checkpointFile.createNewFile();
            if (!fileCreated) {
                System.out.println("Couldn't create file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        EnvVars envVars = run.getEnvironment(taskListener);
        String jobName = envVars.get("JOB_BASE_NAME");
        createCheckpointFile(jobName, run.number);
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