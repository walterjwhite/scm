package com.walterjwhite.scm.api;

import com.walterjwhite.scm.api.model.SCMConfiguration;
import com.walterjwhite.scm.api.model.SCMTag;

public interface SCMService {
  void checkout(SCMConfiguration scmConfiguration) throws Exception;

  SCMTag getSCMTag(SCMConfiguration scmConfiguration) throws Exception;
}
