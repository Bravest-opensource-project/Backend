package opensource.bravest.domain.vote.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VoteDto {

    @Getter
    @NoArgsConstructor
    public static class CreateVoteRequest {
        private Long roomId;
        private List<String> messages;
    }

    @Getter
    @NoArgsConstructor
    public static class CastVoteRequest {
        private Long voteOptionId;
        private Long anonymousProfileId;
    }

    @Getter
    @Builder
    public static class VoteResponse {
        private Long id;
        private String title;
        private boolean isActive;
        private LocalDateTime createdAt;
        private List<VoteOptionResponse> options;
    }

    @Getter
    @Builder
    public static class VoteOptionResponse {
        private Long id;
        private String messageContent;
        private int voteCount;
    }
}
