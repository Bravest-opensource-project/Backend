package opensource.bravest.domain.room.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import opensource.bravest.domain.profile.entity.AnonymousProfile;

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

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<AnonymousProfile> profiles = new ArrayList<>();

  private LocalDateTime createdAt;

  public void updateTitle(String title) {
    this.title = title;
  }
}
