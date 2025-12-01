package opensource.bravest.domain.message.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import opensource.bravest.domain.message.entity.ChatMessage;

public class MessageDto {

    @Getter
    public static class SendMessageRequest {
        private String content;
    }

    @Getter
    @RequiredArgsConstructor
    public static class MessageResponse {
        private final String senderName; // 익명 닉네임
        private final String content;
        private final LocalDateTime createdAt;

        public static MessageResponse from(ChatMessage chatMessage) {
            return new MessageResponse(chatMessage.getSender().getAnonymousName(), chatMessage.getContent(),
                            chatMessage.getCreatedAt());
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class MessageRequest {
        private final Long chatRoomId;
        private final String content;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ChatReadRequest {
        private final Long chatRoomId;
    }
}
