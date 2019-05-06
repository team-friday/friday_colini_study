package com.colini.study.core.channel.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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
    private Channel channel;

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "authority", nullable = false)
    private String authority;

    @Column(name = "registerAt", nullable = false)
    private LocalDateTime registerAt;

    @CreatedDate
    @Column(name = "modifiedAt", nullable = false)
    private LocalDateTime modifiedAt;

    @PrePersist
    public void createdAt() {
        this.registerAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
}
