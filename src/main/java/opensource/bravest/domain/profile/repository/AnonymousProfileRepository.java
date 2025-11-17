package opensource.bravest.domain.profile.repository;

import opensource.bravest.domain.profile.entity.AnonymousProfile;
import opensource.bravest.domain.room.entity.AnonymousRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnonymousProfileRepository extends JpaRepository<AnonymousProfile, Long> {

    // 같은 방 + 같은 실제 유저라면 익명 프로필 하나만 사용
    Optional<AnonymousProfile> findByRoomAndRealUserId(AnonymousRoom room, Long realUserId);
}