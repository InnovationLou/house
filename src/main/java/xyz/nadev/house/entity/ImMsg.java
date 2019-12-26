package xyz.nadev.house.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "im_msg")
public class ImMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "sender_id", nullable = false)
    private Integer senderId;

    @Column(name = "receiver_id", nullable = false)
    private Integer receiverId;

    // mysql enum类型
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "msg")
    private String msg;

    @Column(name = "is_read")
    private boolean read;

    @Column(name = "gmt_send")
    private Date gmtSend;

    @Column(name = "gmt_read")
    private Date gmtRead;

    @Column(name = "house_id")
    private Integer houseId;

}
