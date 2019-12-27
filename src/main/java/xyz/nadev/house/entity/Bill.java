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
 * 这个表用于记录租客的账单
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "bill")
public class Bill {

	/**
	 * null
	 * default value: null
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	/**
	 * 用户id
	 * default value: null
	 */
	@Column(name = "user_id", nullable = true)
	private Integer userId;

	/**
	 * 房子id
	 * default value: null
	 */
	@Column(name = "house_id", nullable = true)
	private Integer houseId;

	/**
	 * 付款金额
	 * default value: null
	 */
	@Column(name = "money", nullable = true)
	private BigDecimal money;

	/**
	 * 支付项目类型  
	 * default value: null
	 */
	@Column(name = "pay_item", nullable = true)
	private String payItem;

	/**
	 * 是否支付
	 * default value: null
	 */
	@Column(name = "is_paid", nullable = true)
	private Integer isPaid;

	/**
	 * 付款单号
	 * default value: null
	 */
	@Column(name = "out_trade_no", nullable = true)
	private String outTradeNo;

	/**
	 * 创建日期 
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "gmt_create", nullable = false)
	@CreatedDate
	private java.util.Date gmtCreate;

	/**
	 * 备注
	 * default value: null
	 */
	@Column(name = "remark", nullable = true)
	private String remark;

	/**
	 * 最迟缴费日期
	 * default value: null
	 */
	@Column(name = "dead_date", nullable = true)
	private java.util.Date deadDate;

	/**
	 * 用户缴费日期
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "pay_date", nullable = true)
	private java.util.Date payDate;

	/**
	 * 支付详细
	 * default value: null
	 */
	@Column(name = "pay_detail1", nullable = true)
	private String payDetail1;

	/**
	 * 支付详细金额
	 * default value: null
	 */
	@Column(name = "pay_detail_fee1", nullable = true)
	private Double payDetailFee1;

	/**
	 * 支付详细
	 * default value: null
	 */
	@Column(name = "pay_detail2", nullable = true)
	private String payDetail2;

	/**
	 * 支付详细金额
	 * default value: null
	 */
	@Column(name = "pay_detail_fee2", nullable = true)
	private Double payDetailFee2;

	/**
	 * 水使用量
	 * default value: null
	 */
	@Column(name = "water_use", nullable = true)
	private String waterUse;

	/**
	 * 水单价
	 * default value: null
	 */
	@Column(name = "water_unt_price", nullable = true)
	private Double waterUntPrice;

	/**
	 * 电使用量
	 * default value: null
	 */
	@Column(name = "ele_use", nullable = true)
	private String eleUse;

	/**
	 * 电单价
	 * default value: null
	 */
	@Column(name = "ele_unt_price", nullable = true)
	private Double eleUntPrice;
}
