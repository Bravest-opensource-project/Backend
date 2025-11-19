package opensource.bravest.domain.chatList.repository;

import opensource.bravest.domain.chatList.entity.ChatList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatListRepository extends JpaRepository<ChatList, Long> {

    @Query("SELECT c FROM ChatList c WHERE c.room.id = :roomId ORDER BY c.createdAt DESC")
    List<ChatList> findAllByRoomId(Long roomId);
}