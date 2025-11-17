package opensource.bravest.domain.message.dto;

import lombok.Getter;

import java.time.LocalDateTime;

public class MessageDto {

    @Getter
    public static class SendMessageRequest {
        private String content;
    }

    @Getter
    public static class MessageResponse {
        private final String senderName;  // 익명 닉네임
        private final String content;
        private final LocalDateTime createdAt;

        public MessageResponse(String senderName, String content, LocalDateTime createdAt) {
            this.senderName = senderName;
            this.content = content;
            this.createdAt = createdAt;
        }
    }
}
