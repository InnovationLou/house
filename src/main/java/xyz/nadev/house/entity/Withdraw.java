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
 * 提现记录表
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "withdraw")
public class Withdraw {

	/**
	 * null
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
	@Column(name = "open_id", nullable = false)
	private String openId;

	/**
	 * 微信号，用于手动打款时使用
	 * default value: null
	 */
	@Column(name = "wx_id", nullable = false)
	private String wxId;

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

	/**
	 * 提款金额
	 * default value: null
	 */
	@Column(name = "money", nullable = true)
	private BigDecimal money;

	/**
	 * 提现处理是否异常  0异常   1无异常
	 * default value: null
	 */
	@Column(name = "withdraw_status", nullable = true)
	private Boolean withdrawStatus;

	/**
	 * 订单是否完成 0未完成 1 完成

	 * default value: null
	 */
	@Column(name = "is_finish", nullable = true)
	private Boolean isFinish;

	/**
	 * 备注
	 * default value: null
	 */
	@Column(name = "remark", nullable = true)
	private String remark;

	/**
	 * 审核中，0位通过，1通过
	 * default value: 1
	 */
	@Column(name = "is_check", nullable = true)
	private Boolean isCheck;

	/**
	 * 打款中，0未打款，1 已经打款
	 * default value: 0
	 */
	@Column(name = "is_check_pass", nullable = true)
	private Boolean isCheckPass;
}
