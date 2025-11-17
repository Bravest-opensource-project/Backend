package opensource.bravest.domain.room.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AnonymousRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 친구들에게 공유하는 코드 (예: ABC123)
    @Column(nullable = false, unique = true, length = 20)
    private String roomCode;

    // 방 제목 (선택)
    @Column(nullable = false, length = 100)
    private String title;

    private LocalDateTime createdAt;
}