package xyz.nadev.house.entity;

import lombok.Data;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 房源浏览记录
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "browse")
public class Browse {

	/**
	 * null
	 * default value: null
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	/**
	 * 用户id
	 * default value: null
	 */
	@Column(name = "user_id", nullable = false)
	private Integer userId;

	/**
	 * 房源id
	 * default value: null
	 */
	@Column(name = "house_id", nullable = false)
	private Integer houseId;

	/**
	 * 浏览时间
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "gmt_create", nullable = false)
	@CreatedDate
	private java.util.Date gmtCreate;
}
