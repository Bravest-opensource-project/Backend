package opensource.bravest.domain.room.repository;

import opensource.bravest.domain.room.entity.AnonymousRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnonymousRoomRepository extends JpaRepository<AnonymousRoom, Long> {

    Optional<AnonymousRoom> findByRoomCode(String roomCode);

    boolean existsByRoomCode(String roomCode);
}