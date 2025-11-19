package opensource.bravest.domain.chatList.repository;

import opensource.bravest.domain.chatList.entity.ChatList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatListRepository extends JpaRepository<ChatList, Long> {

    // 특정 roomId에 해당하는 모든 아이디어 목록을 조회하는 메서드 추가
    List<ChatList> findAllByRoomIdOrderByCreatedAtDesc(Long roomId);
}