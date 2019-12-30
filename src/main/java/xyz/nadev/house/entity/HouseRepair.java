package xyz.nadev.house.entity;

import lombok.Data;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 报修信息表
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "house_repair")
public class HouseRepair {

	/**
	 * null
	 * default value: null
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	/**
	 * 房源ID 关联house表
	 * default value: null
	 */
	@Column(name = "house_id", nullable = false)
	private Integer houseId;

	/**
	 * 报修用户ID
	 * default value: null
	 */
	@Column(name = "user_id", nullable = true)
	private Integer userId;

	/**
	 * 联系电话
	 * default value: null
	 */
	@Column(name = "phone", nullable = false)
	private String phone;

	/**
	 * 报修描述
	 * default value: null
	 */
	@Column(name = "content", nullable = false)
	private String content;

	/**
	 * 报修状态 0 未处理 1  处理中 2 已关闭
	 * default value: 0
	 */
	@Column(name = "status", nullable = false)
	private Integer status;

	/**
	 * 报修创建时间
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "gmt_create", nullable = false)
	@CreatedDate
	private java.util.Date gmtCreate;

	/**
	 * 房源名字
	 * default value: null
	 */
	@Column(name = "house_info", nullable = true)
	private String houseInfo;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "house_img_url", nullable = true)
	private String houseImgUrl;

	@Column(name = "repaire_time")
	private Date repaireTime;

	@Transient
	private House house;
}
