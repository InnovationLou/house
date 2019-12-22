package xyz.nadev.house.entity;

import lombok.Data;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

/**
 * null
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "transfer_record")
public class TransferRecord {

	/**
	 * 这个表用于记录手动打款记录
	 * default value: null
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	/**
	 * 生成的提款订单号
	 * default value: null
	 */
	@Column(name = "withdraw_ment", nullable = false)
	private String withdrawMent;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "wx_id", nullable = true)
	private String wxId;

	/**
	 * 付给用户的钱
	 * default value: null
	 */
	@Column(name = "transfer_money", nullable = true)
	private BigDecimal transferMoney;

	/**
	 * 创建日期
	 * default value: null
	 */
	@Column(name = "gmt_create", nullable = true)
	private java.util.Date gmtCreate;

	/**
	 * 修改日期
	 * default value: null
	 */
	@Column(name = "gmt_modify", nullable = true)
	private java.util.Date gmtModify;
}
