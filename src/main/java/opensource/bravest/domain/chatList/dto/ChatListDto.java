package opensource.bravest.domain.chatList.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import opensource.bravest.domain.chatList.entity.ChatList;

public class ChatListDto {

    // 1. 아이디어 생성 요청 DTO (Create Request)
    @Getter
    @Setter
    public static class ChatListCreateRequest {

        private Long roomId;

        private String content;

        private Long registeredBy;
    }

    // 2. 아이디어 수정 요청 DTO (Update Request)
    @Getter
    @Setter
    public static class ChatListUpdateRequest {

        // 아이디어 내용 수정만 가정
        private String content;
    }

    @Getter
    @Builder
    public static class ChatListResponse {
        private Long id;
        private Long roomId;
        private String content;
        private Long registeredBy;
        private LocalDateTime createdAt;

        public static ChatListResponse fromEntity(ChatList chatList) {
            return ChatListResponse.builder().id(chatList.getId()).roomId(chatList.getRoomId())
                    .content(chatList.getContent()).registeredBy(chatList.getRegisteredBy().getId())
                    .createdAt(chatList.getCreatedAt()).build();
        }
    }
}
