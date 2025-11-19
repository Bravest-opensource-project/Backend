package opensource.bravest.domain.vote.entity;

import jakarta.persistence.*;
import lombok.*;
import opensource.bravest.domain.message.entity.ChatMessage;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    @Column(name = "message_content", nullable = false)
    private String messageContent;

    @Column(nullable = false)
    private int voteCount;

    public void incrementVoteCount() {
        this.voteCount++;
    }
}
