package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "person_transfer")
public class PersonTransfer extends BaseEntity {

  @Column(name = "person_id", nullable = false, length = 36)
  private String personId;

  @Column(name = "from_region_id", length = 36)
  private String fromRegionId;

  @Column(name = "to_region_id", nullable = false, length = 36)
  private String toRegionId;

  @Column(name = "transfer_date", nullable = false)
  private LocalDate transferDate;

  @Column(length = 500)
  private String reason;

  @Column(length = 20)
  private String status;

  public String getPersonId() {
    return personId;
  }

  public void setPersonId(String personId) {
    this.personId = personId;
  }

  public String getFromRegionId() {
    return fromRegionId;
  }

  public void setFromRegionId(String fromRegionId) {
    this.fromRegionId = fromRegionId;
  }

  public String getToRegionId() {
    return toRegionId;
  }

  public void setToRegionId(String toRegionId) {
    this.toRegionId = toRegionId;
  }

  public LocalDate getTransferDate() {
    return transferDate;
  }

  public void setTransferDate(LocalDate transferDate) {
    this.transferDate = transferDate;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}

