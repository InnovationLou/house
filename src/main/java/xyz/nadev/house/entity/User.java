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
 * 用户信息表
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
public class User {

	/**
	 * null
	 * default value: null
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	/**
	 * 微信提供的用户唯一ID(相对于本小程序)
	 * default value: null
	 */
	@Column(name = "open_id", nullable = false)
	private String openId;

	/**
	 * 昵称
	 * default value: null
	 */
	@Column(name = "nick_name", nullable = false)
	private String nickName;

	/**
	 * 所在城市
	 * default value: null
	 */
	@Column(name = "city", nullable = true)
	private String city;

	/**
	 * 省份
	 * default value: null
	 */
	@Column(name = "province", nullable = true)
	private String province;

	/**
	 * 国家
	 * default value: null
	 */
	@Column(name = "country", nullable = true)
	private String country;

	/**
	 * 语言
	 * default value: null
	 */
	@Column(name = "language", nullable = true)
	private String language;

	/**
	 * 性别 0-无,1-男,2-女
	 * default value: 0
	 */
	@Column(name = "gender", nullable = false)
	private Integer gender;

	/**
	 * 钱包余额
	 * default value: 0.00
	 */
	@Column(name = "money", nullable = false)
	private BigDecimal money;

	/**
	 * 创建时间
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "gmt_create", nullable = false)
	@CreatedDate
	private java.util.Date gmtCreate;

	/**
	 * 修改时间
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "gmt_modify", nullable = false)
	@LastModifiedDate
	private java.util.Date gmtModify;

	/**
	 * 是否房东
	 * default value: 0
	 */
	@Column(name = "landlord", nullable = false)
	private Integer landlord;

	/**
	 * 认证了房东 有相关的图片链接 身份证

	 * default value: null
	 */
	@Column(name = "auth_img_url", nullable = true)
	private String authImgUrl;

	/**
	 * 证件正面
	 * default value: null
	 */
	@Column(name = "id_card_front_img", nullable = true)
	private String idCardFrontImg;

	/**
	 * 证件背面
	 * default value: null
	 */
	@Column(name = "id_card_back_img", nullable = true)
	private String idCardBackImg;

	/**
	 * 电子手签图
	 * default value: null
	 */
	@Column(name = "handwritten_signature", nullable = true)
	private String handwrittenSignature;
}
