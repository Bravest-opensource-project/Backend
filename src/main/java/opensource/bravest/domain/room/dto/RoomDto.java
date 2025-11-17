package opensource.bravest.domain.room.dto;

import lombok.Getter;

public class RoomDto {

    @Getter
    public static class CreateRoomRequest {
        private String title;
    }

    @Getter
    public static class CreateRoomResponse {
        private final String roomCode;
        private final String title;

        public CreateRoomResponse(String roomCode, String title) {
            this.roomCode = roomCode;
            this.title = title;
        }
    }

    @Getter
    public static class JoinRoomResponse {
        private final String roomCode;
        private final String title;
        private final String anonymousName;

        public JoinRoomResponse(String roomCode, String title, String anonymousName) {
            this.roomCode = roomCode;
            this.title = title;
            this.anonymousName = anonymousName;
        }
    }
}