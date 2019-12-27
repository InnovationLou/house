package xyz.nadev.house.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * 用户收藏店铺信息
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "favor_store")
public class FavorStore {

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
	 * 商铺Id
	 * default value: null
	 */
	@Column(name = "store_id", nullable = true)
	private Integer storeId;

	/**
	 * 创建时间
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "gmt_create", nullable = true)
	@CreatedDate
	private java.util.Date gmtCreate;
}
