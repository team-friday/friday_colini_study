package com.colini.study.core.channel.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name="CHANNEL")
@NoArgsConstructor
@AllArgsConstructor
public class Channel implements Serializable {

    @Builder
    public Channel(String userName, String channelName) {
        this.userName = userName;
        this.channelName = channelName;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)  //hibernate 5.2 AUTO -> IDENTITY
    @Column(name = "channelId", nullable=false)
    private long channelId;

    @Column(name= "userName", nullable=false)
    private String userName;

    @Column(name="channelName", nullable=false)
    private String channelName;

    @CreatedDate
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChannelMember> channelMembers = new ArrayList<>();
}
