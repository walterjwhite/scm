package com.walterjwhite.scm.providers.jgit;

import com.walterjwhite.scm.api.SCMService;
import com.walterjwhite.scm.api.model.SCMConfiguration;
import com.walterjwhite.scm.api.model.SCMTag;
import com.walterjwhite.shell.api.service.ShellExecutionService;
import com.walterjwhite.shell.impl.service.ShellCommandBuilder;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.inject.Inject;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

// TODO: this API supports diffing, that would be useful for performing delta builds (only doing *
// what changed).
// https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/CloneRemoteRepository.java
public class JGitGitSCMService implements SCMService {

  @Inject
  public GitSCMService(
      ShellCommandBuilder shellCommandBuilder, ShellExecutionService shellExecutionService) {
    super();
  }

  @Override
  public void checkout(SCMConfiguration scmConfiguration) throws Exception {
    FileRepositoryBuilder fileRepositoryBuilder = new FileRepositoryBuilder();
    Repository repository = fileRepositoryBuilder.build();
    try (Git git = new Git(repository)) {
      // git.checkout().
    }

    // the permissions are incorrect
    try (Git result =
        Git.cloneRepository()
            .setURI(buildConfiguration.getRepositoryUri())
            .setDirectory(localRepositoryFile)
            .setBranch(buildConfiguration.getTag())
            // .setCredentialsProvider()
            .call()) {
      // Note: the call() returns an opened repository already which needs to be closed to avoid
      // file handle leaks!
    }
  }

  @Override
  public SCMTag getSCMTag(SCMConfiguration scmConfiguration) throws IOException {
    FileRepositoryBuilder fileRepositoryBuilder = new FileRepositoryBuilder();
    fileRepositoryBuilder.setWorkTree(new File(scmConfiguration.getWorkspacePath()));
    Repository repository = fileRepositoryBuilder.build();

    final Ref ref = repository.exactRef(getRef(scmConfiguration));

    try (RevWalk walk = new RevWalk(repository)) {
      RevCommit commit = walk.parseCommit(ref.getObjectId());

      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(((long) commit.getCommitTime() * 1000));
      final SCMTag scmTag =
          SCMTag.builder()
              .tag(scmConfiguration.getTag().getTag())
              // .variant(scmConfiguration.getVariant())
              .commitDate(calendar.getTime())
              .buildDate(new Date())
              .scmVersionId(commit.toString())
              .commitMessage(commit.getFullMessage())
              .build();
      walk.dispose();
      return (scmTag);
    }
    // create a new patch
    // write build date, tag, tag date, commit ref
  }

  protected String getRef(SCMConfiguration scmConfiguration) {
    return "refs/heads/" + scmConfiguration.getTag();
  }

  // public void setup(){}
}
