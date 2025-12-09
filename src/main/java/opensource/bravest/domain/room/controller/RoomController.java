package opensource.bravest.domain.room.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import opensource.bravest.domain.room.dto.RoomDto;
import opensource.bravest.domain.room.entity.AnonymousRoom;
import opensource.bravest.domain.room.service.RoomService;
import opensource.bravest.global.apiPayload.ApiResponse;
import opensource.bravest.global.apiPayload.code.status.SuccessStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @Operation(summary = "채팅방 생성", description = "새로운 채팅방을 생성합니다.")
    public ApiResponse<RoomDto.RoomResponse> createRoom(@RequestBody RoomDto.CreateRoomRequest request) {
        AnonymousRoom room = roomService.createRoom(request);
        return ApiResponse.of(SuccessStatus._CREATED, SuccessStatus._CREATED.getMessage(),
                        RoomDto.RoomResponse.builder().id(room.getId()).roomCode(room.getRoomCode())
                                        .title(room.getTitle()).createdAt(room.getCreatedAt()).build());
    }

    @GetMapping("/{roomId}")
    @Operation(summary = "채팅방 조회", description = "ID로 특정 채팅방의 정보를 조회합니다.")
    public ApiResponse<RoomDto.RoomResponse> getRoom(@PathVariable Long roomId) {
        AnonymousRoom room = roomService.getRoom(roomId);
        return ApiResponse.of(SuccessStatus._OK, SuccessStatus._OK.getMessage(),
                        RoomDto.RoomResponse.builder().id(room.getId()).roomCode(room.getRoomCode())
                                        .title(room.getTitle()).createdAt(room.getCreatedAt()).build());
    }

    @PutMapping("/{roomId}")
    @Operation(summary = "채팅방 정보 수정", description = "ID로 특정 채팅방의 정보를 수정합니다.")
    public ApiResponse<RoomDto.RoomResponse> updateRoom(@PathVariable Long roomId,
                    @RequestBody RoomDto.UpdateRoomRequest request) {
        AnonymousRoom room = roomService.updateRoom(roomId, request);
        return ApiResponse.of(SuccessStatus._OK, SuccessStatus._OK.getMessage(),
                        RoomDto.RoomResponse.builder().id(room.getId()).roomCode(room.getRoomCode())
                                        .title(room.getTitle()).createdAt(room.getCreatedAt()).build());
    }

    @DeleteMapping("/{roomId}")
    @Operation(summary = "채팅방 삭제", description = "ID로 특정 채팅방을 삭제합니다.")
    public ApiResponse<?> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ApiResponse.of(SuccessStatus._OK, SuccessStatus._OK.getMessage(), null);
    }

    @GetMapping("/{roomId}/invite-code")
    @Operation(summary = "초대 코드 조회", description = "ID로 특정 채팅방의 초대 코드를 조회합니다.")
    public ApiResponse<String> getInviteCode(@PathVariable Long roomId) {
        String inviteCode = roomService.getInviteCode(roomId);
        return ApiResponse.of(SuccessStatus._OK, SuccessStatus._OK.getMessage(), inviteCode);
    }

    @PostMapping("/join")
    @Operation(summary = "초대 코드로 채팅방 참여", description = "초대 코드를 사용하여 특정 채팅방에 참여합니다.")
    public ApiResponse<RoomDto.RoomResponse> joinRoom(@RequestBody RoomDto.JoinRoomRequest request) {
        AnonymousRoom room = roomService.joinRoom(request.getRoomCode());
        return ApiResponse.of(SuccessStatus._OK, SuccessStatus._OK.getMessage(),
                        RoomDto.RoomResponse.builder().id(room.getId()).roomCode(room.getRoomCode())
                                        .title(room.getTitle()).createdAt(room.getCreatedAt()).build());
    }
}
