package xyz.nadev.house.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
//@EntityListeners(AuditingEntityListener.class)
//@DynamicUpdate
//@DynamicInsert
@Table(name = "recommend")
public class Recommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "img_url",nullable = true)
    private String imgUrl;

    @Column(name = "house_id",nullable = false)
    private Integer houseId;

    @Column(name = "gmt_create",nullable = true)
    private Date gmtCreate;

    @Column(name = "gmt_modified",nullable = true)
    private Date gmtModified;

    @Column(name = "tip",nullable = true)
    private String tip;
}
