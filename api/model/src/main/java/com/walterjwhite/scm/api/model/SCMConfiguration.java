package com.walterjwhite.scm.api.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(doNotUseGetters = true)
public class SCMConfiguration {
  protected String workspacePath;
  protected SCMTag tag;
  protected String repositoryUri;
}
