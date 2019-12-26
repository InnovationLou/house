package xyz.nadev.house.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * null
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
	@Column(name = "gmt_create", nullable = true)
	@CreatedDate
	private java.util.Date gmtCreate;

	/**
	 * 是否履行中 
	 * default value: null
	 */
	@Column(name = "is_fulfill", nullable = true)
	private Integer isFulfill;

	/**
	 * 到期时间
	 * default value: null
	 */
	@Column(name = "end_create", nullable = true)
	private java.util.Date endCreate;

	/**
	 * 有效期
	 * default value: null
	 */
	@Column(name = "exp_date", nullable = true)
	private Integer expDate;
}
