package opensource.bravest.domain.chatList.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import opensource.bravest.domain.profile.entity.AnonymousProfile;
import opensource.bravest.domain.room.entity.AnonymousRoom;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_list")
public class ChatList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private AnonymousRoom room;

    @NotNull
    @Column(length = 255)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private AnonymousProfile registeredBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public ChatList(AnonymousRoom room, String content, AnonymousProfile registeredBy) {
        this.room = room;
        this.content = content;
        this.registeredBy = registeredBy;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public Long getRoomId() {
        return this.room.getId();
    }

    public Long getProfileId() {
        return this.registeredBy.getId();
    }
}
