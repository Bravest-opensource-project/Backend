package opensource.bravest.domain.room.service;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import opensource.bravest.domain.room.dto.RoomDto;
import opensource.bravest.domain.room.entity.AnonymousRoom;
import opensource.bravest.domain.room.repository.AnonymousRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final AnonymousRoomRepository anonymousRoomRepository;

    @Transactional
    public AnonymousRoom createRoom(RoomDto.CreateRoomRequest request) {
        String roomCode = generateUniqueRoomCode();
        AnonymousRoom room = AnonymousRoom.builder().title(request.getTitle()).roomCode(roomCode)
                        .createdAt(LocalDateTime.now()).build();
        return anonymousRoomRepository.save(room);
    }

    public AnonymousRoom getRoom(Long roomId) {
        return anonymousRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
    }

    @Transactional
    public AnonymousRoom updateRoom(Long roomId, RoomDto.UpdateRoomRequest request) {
        AnonymousRoom room = getRoom(roomId);
        room.updateTitle(request.getTitle());
        return room;
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        if (!anonymousRoomRepository.existsById(roomId)) {
            throw new RuntimeException("Room not found");
        }
        anonymousRoomRepository.deleteById(roomId);
    }

    public String getInviteCode(Long roomId) {
        AnonymousRoom room = getRoom(roomId);
        return room.getRoomCode();
    }

    public AnonymousRoom joinRoom(String roomCode) {
        return anonymousRoomRepository.findByRoomCode(roomCode)
                        .orElseThrow(() -> new RuntimeException("Room not found with code: " + roomCode));
    }

    private String generateUniqueRoomCode() {
        String roomCode;
        do {
            roomCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (anonymousRoomRepository.existsByRoomCode(roomCode));
        return roomCode;
    }
}
