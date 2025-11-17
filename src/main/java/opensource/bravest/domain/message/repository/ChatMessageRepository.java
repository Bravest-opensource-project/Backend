package opensource.bravest.domain.message.repository;


import opensource.bravest.domain.message.entity.ChatMessage;
import opensource.bravest.domain.room.entity.AnonymousRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 방 기준으로 최근 메시지 목록
    List<ChatMessage> findByRoomOrderByCreatedAtAsc(AnonymousRoom room);
}
