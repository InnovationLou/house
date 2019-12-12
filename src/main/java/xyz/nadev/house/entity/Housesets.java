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
@Table(name = "housesets")
public class Housesets {

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
	@Column(name = "fdId", nullable = false)
	private String fdid;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "houseinfo", nullable = false)
	private String houseinfo;

	/**
	 * null
	 * default value: 'no'
	 */
	@Column(name = "isrelease", nullable = false)
	private String isrelease;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "imageindex", nullable = false)
	private String imageindex;

	/**
	 * null
	 * default value: 'no'
	 */
	@Column(name = "isout", nullable = false)
	private String isout;

	/**
	 * null
	 * default value: '0'
	 */
	@Column(name = "starttime", nullable = false)
	private String starttime;

	/**
	 * null
	 * default value: '0'
	 */
	@Column(name = "endtime", nullable = false)
	private String endtime;

	/**
	 * null
	 * default value: 'k'
	 */
	@Column(name = "joinpeople", nullable = true)
	private String joinpeople;

	/**
	 * null
	 * default value: 'k'
	 */
	@Column(name = "scpeople", nullable = true)
	private String scpeople;

	/**
	 * null
	 * default value: 'no'
	 */
	@Column(name = "outholder", nullable = false)
	private String outholder;

	/**
	 * null
	 * default value: 'no'
	 */
	@Column(name = "holderinfo", nullable = false)
	private String holderinfo;

	/**
	 * null
	 * default value: 'no'
	 */
	@Column(name = "userholderpic", nullable = false)
	private String userholderpic;
}
