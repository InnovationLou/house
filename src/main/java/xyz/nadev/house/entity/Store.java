package xyz.nadev.house.entity;

import lombok.Data;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 商家信息
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "store")
public class Store {

	/**
	 * null
	 * default value: null
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	/**
	 * 商家名称
	 * default value: null
	 */
	@Column(name = "name", nullable = true)
	private String name;

	/**
	 * 标题图
	 * default value: null
	 */
	@Column(name = "head_img", nullable = true)
	private String headImg;

	/**
	 * 商家地址
	 * default value: null
	 */
	@Column(name = "address", nullable = true)
	private String address;

	/**
	 * 营业时间段
	 * default value: null
	 */
	@Column(name = "opening_hours", nullable = true)
	private String openingHours;

	/**
	 * 联系电话
	 * default value: null
	 */
	@Column(name = "phone", nullable = true)
	private String phone;

	/**
	 * 商家分类 零食便利 美容美发餐饮美食
	 * default value: null
	 */
	@Column(name = "type", nullable = true)
	private String type;
}
