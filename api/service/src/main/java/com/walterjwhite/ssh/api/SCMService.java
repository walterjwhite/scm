package com.walterjwhite.ssh.api;

import com.walterjwhite.ssh.api.model.SCMConfiguration;
import com.walterjwhite.ssh.api.model.SCMTag;

public interface SCMService {
  void checkout(SCMConfiguration scmConfiguration) throws Exception;

  SCMTag getSCMTag(SCMConfiguration scmConfiguration) throws Exception;
}
