package opensource.bravest.domain.message.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import opensource.bravest.domain.profile.entity.AnonymousProfile;
import opensource.bravest.domain.room.entity.AnonymousRoom;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어느 방의 메시지인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private AnonymousRoom room;

    // 누가 보냈는지 (익명 프로필 기준)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anonymous_profile_id", nullable = false)
    private AnonymousProfile sender;

    @Column(nullable = false, length = 1000)
    private String content;

    private LocalDateTime createdAt;
}
