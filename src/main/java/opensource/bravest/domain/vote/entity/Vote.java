package opensource.bravest.domain.vote.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import opensource.bravest.domain.room.entity.AnonymousRoom;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Vote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id", nullable = false)
  private AnonymousRoom room;

  @Column(nullable = false, length = 100)
  private String title;

  @Builder.Default
  @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<VoteOption> options = new ArrayList<>();

  private boolean isActive;

  private LocalDateTime createdAt;

  public void endVote() {
    this.isActive = false;
  }
}
