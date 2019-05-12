package com.colini.study.core.channel.domain.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity(name = "CHANNEL_MEMBER")
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMember implements Serializable {

    @Builder
    public ChannelMember(Channel channel, String userName, String authority) {
        this.channel = channel;
        this.userName = userName;
        this.authority = authority;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(targetEntity = Channel.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name ="channelId")
    @JsonBackReference
    private Channel channel;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "authority", nullable = false)
    private String authority;

    @Column(name = "registerAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerAt;

    @CreatedDate
    @Column(name = "modifiedAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
}
