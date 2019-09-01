package com.walterjwhite.scm.api.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString(doNotUseGetters = true)
public class SCMTag {
  protected String tag;

  // protected String variant;

  // protected Date buildDate;
  protected String scmVersionId;

  protected String commitMessage;

  protected LocalDateTime commitDate;

  protected String commitAuthor;
}
