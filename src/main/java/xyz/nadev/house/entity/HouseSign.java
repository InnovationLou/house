package xyz.nadev.house.entity;

import lombok.Data;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 用户签约信息
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "house_sign")
public class HouseSign {

	/**
	 * 这个表用于记录用户签约信息
	 * default value: null
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "user_id", nullable = true)
	private Integer userId;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "house_id", nullable = true)
	private Integer houseId;

	/**
	 * 签约时间
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "start_date", nullable = true)
	private java.util.Date startDate;

	/**
	 * 到期时间
	 * default value: null
	 */
	@Column(name = "end_date", nullable = true)
	private java.util.Date endDate;

	/**
	 * 有效期,单位为月
	 * default value: null
	 */
	@Column(name = "exp_date", nullable = true)
	private Integer expDate;

	/**
	 * 订单创建时间
	 * default value: null
	 */
	@Column(name = "gmt_create", nullable = true)
	private java.util.Date gmtCreate;

	/**
	 * 修改时间
	 * default value: null
	 */
	@Column(name = "gmt_modified", nullable = true)
	private java.util.Date gmtModified;

	/**
	 * 付款账单号
	 * default value: null
	 */
	@Column(name = "out_trade_no", nullable = true)
	private String outTradeNo;

	/**
	 * 是否支付  0未支付，1支付
	 * default value: null
	 */
	@Column(name = "is_paid", nullable = true)
	private Integer isPaid;

	/**
	 * 是否履行中 0否1是
	 * default value: null
	 */
	@Column(name = "is_fulfill", nullable = true)
	private Integer isFulfill;

	/**
	 * 手写签名的地址
	 * default value: null
	 */
	@Column(name = "hand_write_img", nullable = true)
	private String handWriteImg;

	/**
	 * 合同
	 * default value: null
	 */
	@Column(name = "contract_img", nullable = true)
	private String contractImg;

	/**
	 *  签订用户的姓名
	 * default value: null
	 */
	@Column(name = "user_name", nullable = true)
	private String userName;

	/**
	 * 签订用户的身份证号码
	 * default value: null
	 */
	@Column(name = "idcard_num", nullable = true)
	private String idcardNum;

	/**
	 * 签约是否过期
	 * default value: null
	 */
	@Column(name = "is_out", nullable = true)
	private Integer isOut;

	/**
	 * 是否已作废,
	 * default value: null
	 */
	@Column(name = "is_deleted", nullable = true)
	private Integer isDeleted;

	/**
	 * 哈哈, vo属性, 与数据库无关
	 */
	@Transient
	private House house;
}
