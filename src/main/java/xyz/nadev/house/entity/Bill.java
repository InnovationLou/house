package xyz.nadev.house.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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

@Table(name = "bill")
public class Bill {


	/**
	 * 这个表用于记录租客的账单
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
	 * 备注
	 * default value: null
	 */
	@Column(name = "remark", nullable = true)
	private String remark;

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
	 * 付款单号
	 * default value: null
	 */
	@Column(name = "out_trade_no", nullable = true)
	private String outTradeNo;

	/**
	 * 是否支付
	 * default value: null
	 */
	@Column(name = "is_paid", nullable = true)
	private Integer isPaid;

	/**
	 * 创建日期 
	 * default value: null
	 */
	@Column(name = "gmt_create", nullable = true)
	private java.util.Date  gmtCreate;

	@Column(name = "dead_date", nullable = true)
	private java.util.Date  deadDate;

}
