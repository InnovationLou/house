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
@Table(name = "systemmsg")
public class Systemmsg {

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
	@Column(name = "time", nullable = false)
	private String time;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "con", nullable = false)
	private String con;

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
	@Column(name = "isdelete", nullable = false)
	private String isdelete;
}
