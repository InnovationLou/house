package xyz.nadev.house.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * null
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "house_order")
public class HouseOrder {

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
	@Column(name = "user_id", nullable = false)
	private Integer userId;
	@Column(name = "open_id", nullable = false)
	private String openId;


	/**
	 * 随机生成的订单号
	 * default value: null
	 */
	@Column(name = "out_trade_no", nullable = false)
	private String outTradeNo;

	/**
	 * 微信生成预支付订单号
	 * default value: null
	 */
	@Column(name = "prepay_id", nullable = true)
	private String prepayId;

	/**
	 * 总金额
	 * default value: 0.00
	 */
	@Column(name = "total_fee", nullable = true)
	private BigDecimal totalFee;

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
	 * 是否已经支付,0未支付,1已支付
	 * default value: null
	 */
	@Column(name = "is_paid", nullable = true)
	private Integer isPaid;

	/**
	 * 是否已完成，后台判断了密码后，才应该确认更改此字段
	 * default value: null
	 */
	@Column(name = "is_finished", nullable = true)
	private Integer isFinished;

	/**
	 * 用户付款IP
	 * default value: null
	 */
	@Column(name = "addr_ip", nullable = true)
	private String addrIp;
	@Column(name = "pay_item")
	private String payItem;
	@Column(name = "house_id")
	private Integer houseId;
}
