package xyz.nadev.house.entity;

import lombok.Data;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 房源信息
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "house")
public class House {

	/**
	 * null
	 * default value: null
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;

	/**
	 * 房东用户id
	 * default value: null
	 */
	@Column(name = "user_id", nullable = false)
	private Integer userId;

	/**
	 * 房屋信息
	 * default value: null
	 */
	@Column(name = "house_info", nullable = false)
	private String houseInfo;

	/**
	 * 是否发布
	 * default value: 0
	 */
	@Column(name = "released", nullable = false)
	private Boolean released;

	/**
	 * 是否出租
	 * default value: 0
	 */
	@Column(name = "rented", nullable = false)
	private Boolean rented;

	/**
	 * 房客,租房者id
	 * default value: null
	 */
	@Column(name = "tenant_id", nullable = false)
	private Integer tenantId;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "gmt_start", nullable = false)
	private java.util.Date gmtStart;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "gmt_end", nullable = false)
	private java.util.Date gmtEnd;

	/**
	 * 租金
	 * default value: null
	 */
	@Column(name = "cash", nullable = true)
	private Integer cash;

	/**
	 * 租金付款类型 eg: 押一付三
	 * default value: null
	 */
	@Column(name = "cash_type", nullable = true)
	private String cashType;

	/**
	 * 面积
	 * default value: null
	 */
	@Column(name = "area", nullable = true)
	private Double area;

	/**
	 * 所在楼层
	 * default value: null
	 */
	@Column(name = "floor", nullable = true)
	private Boolean floor;

	/**
	 * null
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "gmt_create", nullable = true)
	@CreatedDate
	private java.util.Date gmtCreate;

	/**
	 * null
	 * default value: CURRENT_TIMESTAMP
	 */
	@Column(name = "gmt_modify", nullable = true)
	@LastModifiedDate
	private java.util.Date gmtModify;

	/**
	 * 房屋朝向
	 * default value: null
	 */
	@Column(name = "orientation", nullable = false)
	private String orientation;

	/**
	 * 采光
	 * default value: null
	 */
	@Column(name = "daylighting", nullable = true)
	private String daylighting;

	/**
	 * null
	 * default value: null
	 */
	@Column(name = "textarea", nullable = true)
	private String textarea;

	/**
	 * 是否有电梯
	 * default value: 0
	 */
	@Column(name = "has_elevator", nullable = true)
	private Boolean hasElevator;

	/**
	 * 是否有电视
	 * default value: 0
	 */
	@Column(name = "has_televison", nullable = true)
	private Boolean hasTelevison;

	/**
	 * 是否有冰箱
	 * default value: 0
	 */
	@Column(name = "has_refrigerator", nullable = true)
	private Boolean hasRefrigerator;

	/**
	 * 是否有洗衣机
	 * default value: 0
	 */
	@Column(name = "has_washer", nullable = true)
	private Boolean hasWasher;

	/**
	 * 是否有空调
	 * default value: 0
	 */
	@Column(name = "has_air_conditioner", nullable = true)
	private Boolean hasAirConditioner;

	/**
	 * 热水器
	 * default value: 0
	 */
	@Column(name = "has_heater", nullable = true)
	private Boolean hasHeater;

	/**
	 * 床
	 * default value: 0
	 */
	@Column(name = "has_bed", nullable = true)
	private Boolean hasBed;

	/**
	 * 暖气
	 * default value: 0
	 */
	@Column(name = "has_heating", nullable = true)
	private Boolean hasHeating;

	/**
	 * 宽带
	 * default value: 0
	 */
	@Column(name = "has_bordband", nullable = true)
	private Boolean hasBordband;

	/**
	 * 衣柜
	 * default value: 0
	 */
	@Column(name = "has_wardrobe", nullable = true)
	private Boolean hasWardrobe;

	/**
	 * 煤气
	 * default value: 0
	 */
	@Column(name = "has_gas", nullable = true)
	private Boolean hasGas;

	/**
	 * 省份
	 * default value: null
	 */
	@Column(name = "province", nullable = true)
	private String province;

	/**
	 * 市
	 * default value: null
	 */
	@Column(name = "city", nullable = true)
	private String city;

	/**
	 * 区县
	 * default value: null
	 */
	@Column(name = "district", nullable = true)
	private String district;

	/**
	 * 街道
	 * default value: null
	 */
	@Column(name = "street", nullable = true)
	private String street;

	/**
	 * 门牌号
	 * default value: null
	 */
	@Column(name = "street_number", nullable = true)
	private String streetNumber;

	/**
	 * 纬度
	 * default value: null
	 */
	@Column(name = "lat", nullable = true)
	private Double lat;

	/**
	 * 经度
	 * default value: null
	 */
	@Column(name = "lng", nullable = true)
	private Double lng;
}
