package opensource.bravest.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class RoomDto {

    @Getter
    @NoArgsConstructor
    public static class CreateRoomRequest {
        private String title;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateRoomRequest {
        private String title;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomResponse {
        private Long id;
        private String roomCode;
        private String title;
        private LocalDateTime createdAt;
    }

    @Getter
    @NoArgsConstructor
    public static class JoinRoomRequest {
        private String roomCode;
    }
}
