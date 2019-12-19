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
@Table(name = "transfer_record")
public class TransferRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "withdraw_ment")
  private String withdrawMent;
  @Column(name = "wx_id")
  private String wxId;
  @Column(name = "transfer_money")
  private BigDecimal transferMoney;
  @Column(name = "gmt_create")
  private  java.util.Date gmtCreate;
  @Column(name = "gmt_modify")
  private  java.util.Date gmtModify;


}
