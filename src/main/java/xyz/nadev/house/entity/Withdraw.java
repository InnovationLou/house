package xyz.nadev.house.entity;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "withdraw")
public class Withdraw {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private long id;
  @Column(name = "open_id")
  private String openId;
  @Column(name = "withdraw_ment")
  private String withdrawMent;
  @Column(name = "gmt_create")
  private java.util.Date gmtCreate;
  @Column(name = "gmt_modify")
  private java.util.Date gmtModify;
  @Column(name = "money")
  private BigDecimal money;
  @Column(name = "is_finished")
  private Integer isFinished;

  @Column(name = "withdraw_status")
  private Integer withdrawStatus;


}
