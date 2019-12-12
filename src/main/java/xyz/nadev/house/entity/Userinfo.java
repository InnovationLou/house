package xyz.nadev.house.entity;

import lombok.Data;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * null
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "userinfo")
public class Userinfo {

	/**
	 * null
	 * default value: null
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "openid", nullable = false)
	private String openid;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "info", nullable = false)
	private String info;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "cash", nullable = false)
	private String cash;

	/**
	 * null
	 * default value: 'no'
	 */
	@Column(name = "isfd", nullable = true)
	private String isfd;

	/**
	 * null
	 * default value: 'no'
	 */
	@Column(name = "authPath", nullable = true)
	private String authpath;
}
