package opensource.bravest.domain.room.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
