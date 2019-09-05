package com.walterjwhite.scm.providers.git.cli;

import com.walterjwhite.shell.api.service.ShellExecutionService;
import com.walterjwhite.shell.impl.service.ShellCommandBuilder;
import com.walterjwhite.ssh.api.SCMService;
import com.walterjwhite.ssh.api.model.SCMConfiguration;
import com.walterjwhite.ssh.api.model.SCMTag;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;

// TODO: this API supports diffing, that would be useful for performing delta builds (only doing *
// what changed).
// https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/CloneRemoteRepository.java
public class GitSCMService implements SCMService {
  protected final ShellCommandBuilder shellCommandBuilder;
  protected final ShellExecutionService shellExecutionService;

  @Inject
  public GitSCMService(
      ShellCommandBuilder shellCommandBuilder, ShellExecutionService shellExecutionService) {
    super();
    this.shellCommandBuilder = shellCommandBuilder;
    this.shellExecutionService = shellExecutionService;
  }

  @Override
  public void checkout(SCMConfiguration scmConfiguration) throws Exception {
    shellExecutionService.run(
        shellCommandBuilder
            .build()
            .withCommandLine(
                "git clone -q "
                    + scmConfiguration.getRepositoryUri()
                    + " -b "
                    + scmConfiguration.getTag().getTag()
                    + " "
                    + scmConfiguration.getWorkspacePath()));

    // this "glue" should happen outside here
    scmConfiguration.setTag(getSCMTag(scmConfiguration));
  }

  // git commit date
  // git commit hash
  // git commit message
  // current date/time
  @Override
  public SCMTag getSCMTag(SCMConfiguration scmConfiguration) {
    try {
      return SCMTag.builder()
          .tag(scmConfiguration.getTag().getTag())
          // null,//buildConfiguration.getVariant(),
          .commitDate(getCommitDateTime(scmConfiguration))
          // new Date(),
          .scmVersionId(getCommitHash(scmConfiguration))
          .commitMessage(getCommitMessage(scmConfiguration))
          .build();
    } catch (Exception e) {
      throw new RuntimeException("Unable to get git details", e);
    }
  }

  // TODO: see the history for this file and migrate the jgit portion of the code (from the history)
  // to jgit
  private static final String COMMIT_HASH_FORMAT = "H";
  private static final String COMMIT_DATE_FORMAT = "cI";
  private static final String COMMIT_SUBJECT_FORMAT = "s";
  private static final String COMMIT_SHOW_TEMPLATE = "git show -s --format=%";

  protected String getCommitHash(SCMConfiguration scmConfiguration) throws Exception {
    return getCommitDetails(scmConfiguration, COMMIT_HASH_FORMAT);
  }

  protected LocalDateTime getCommitDateTime(SCMConfiguration scmConfiguration) throws Exception {
    final String dateString = getCommitDateString(scmConfiguration);

    return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  protected String getCommitDateString(SCMConfiguration scmConfiguration) throws Exception {
    return getCommitDetails(scmConfiguration, COMMIT_DATE_FORMAT);
  }

  protected String getCommitMessage(SCMConfiguration scmConfiguration) throws Exception {
    return getCommitDetails(scmConfiguration, COMMIT_SUBJECT_FORMAT);
  }

  protected String getCommitDetails(SCMConfiguration scmConfiguration, final String format)
      throws Exception {
    return shellExecutionService
        .run(
            shellCommandBuilder
                .build()
                .withCommandLine(COMMIT_SHOW_TEMPLATE + format)
                .withWorkingDirectory(scmConfiguration.getWorkspacePath()))
        .getOutputs()
        .get(0)
        .getOutput();
  }
}
