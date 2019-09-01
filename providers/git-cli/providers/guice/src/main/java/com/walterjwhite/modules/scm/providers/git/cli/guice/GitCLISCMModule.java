package com.walterjwhite.modules.scm.providers.git.cli.guice;

import com.google.inject.AbstractModule;
import com.walterjwhite.scm.api.SCMService;
import com.walterjwhite.scm.providers.git.cli.GitSCMService;

public class GitCLISCMModule extends AbstractModule {
  @Override
  protected void configure() {
    // bind(SCMManagementService.class).to(DefaultSCMManagementService.class);
    bind(SCMService.class).to(GitSCMService.class);
  }
}
