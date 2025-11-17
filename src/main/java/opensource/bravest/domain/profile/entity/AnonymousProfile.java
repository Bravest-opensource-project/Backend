package opensource.bravest.domain.profile.entity;

import jakarta.persistence.*;
import lombok.*;
import opensource.bravest.domain.room.entity.AnonymousRoom;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AnonymousProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 방에 속한 익명 프로필인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private AnonymousRoom room;

    // 실제 유저 PK (User 테이블 없다면 JWT의 userId 기준으로)
    @Column(nullable = false)
    private Long realUserId;

    // 방 안에서 보여줄 익명 닉네임 (예: BlueTiger12)
    @Column(nullable = false, length = 50)
    private String anonymousName;
}