package xyz.nadev.house.entity;

import lombok.Data;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 记录平台给房东打款
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "pay_owner")
public class PayOwner {

	/**
	 * null
	 * default value: null
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	/**
	 * 用户id
	 * default value: null
	 */
	@Column(name = "open_id", nullable = false)
	private String openId;

	/**
	 * 订单号
	 * default value: null
	 */
	@Column(name = "payment", nullable = true)
	private String payment;

	/**
	 * 订单生成时间
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "gmt_create", nullable = false)
	@CreatedDate
	private java.util.Date gmtCreate;

	/**
	 * 订单状态更改时间
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "gmt_modify", nullable = false)
	@LastModifiedDate
	private java.util.Date gmtModify;

	/**
	 * 付款金额
	 * default value: 0.00
	 */
	@Column(name = "money", nullable = false)
	private Double money;
}
