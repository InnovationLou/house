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
@Table(name = "refund_user")
public class RefundUser {

	/**
	 * 这个表用来给退款给用户
	 * default value: null
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	/**
	 * 微信提供的用户唯一ID(相对于本小程序)
	 * default value: null
	 */
	@Column(name = "open_id", nullable = false)
	private String openId;

	/**
	 * 付款的时候自己生成的订单号
	 * default value: null
	 */
	@Column(name = "out_trade_no", nullable = false)
	private String outTradeNo;

	/**
	 * 订单生成时间
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "gmt_create", nullable = true)
	@CreatedDate
	private java.util.Date gmtCreate;

	/**
	 * 订单状态更改时间
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "gmt_modify", nullable = true)
	@LastModifiedDate
	private java.util.Date gmtModify;

	/**
	 * 付款金额
	 * default value: 0.00
	 */
	@Column(name = "money", nullable = true)
	private BigDecimal money;

	/**
	 * 0为退款失败，1为退款成功
	 * default value: null
	 */
	@Column(name = "is_success", nullable = true)
	private Boolean isSuccess;
}
