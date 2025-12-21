package com.it.quanxianguanli.dto;

import java.time.LocalDateTime;

/**
 * 基础VO类，包含通用字段
 */
public class BaseVO {
  private String id;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;

  public BaseVO() {
  }

  public BaseVO(String id, LocalDateTime createTime, LocalDateTime updateTime) {
    this.id = id;
    this.createTime = createTime;
    this.updateTime = updateTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }
}
