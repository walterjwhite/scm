package com.walterjwhite.scm.providers.jgit;

import com.google.inject.AbstractModule;
import com.walterjwhite.scm.api.SCMService;
import com.walterjwhite.scm.providers.git.cli.GitSCMService;

public class JGitSCMModule extends AbstractModule {
  @Override
  protected void configure() {
    // bind(SCMManagementService.class).to(DefaultSCMManagementService.class);
    bind(SCMService.class).to(GitSCMService.class);
  }
}
