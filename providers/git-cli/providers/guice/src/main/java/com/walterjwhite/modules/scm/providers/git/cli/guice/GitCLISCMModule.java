package com.walterjwhite.modules.scm.providers.git.cli.guice;

import com.google.inject.AbstractModule;
import com.walterjwhite.scm.providers.git.cli.GitSCMService;
import com.walterjwhite.ssh.api.SCMService;

public class GitCLISCMModule extends AbstractModule {
  @Override
  protected void configure() {
    // bind(SCMManagementService.class).to(DefaultSCMManagementService.class);
    bind(SCMService.class).to(GitSCMService.class);
  }
}
