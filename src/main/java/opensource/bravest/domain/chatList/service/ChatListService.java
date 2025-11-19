package opensource.bravest.domain.chatList.service;

import static opensource.bravest.domain.chatList.dto.ChatListDto.ChatListResponse;
import static opensource.bravest.domain.chatList.dto.ChatListDto.ChatListUpdateRequest;
import static opensource.bravest.domain.chatList.dto.ChatListDto.ChatListCreateRequest;
import static opensource.bravest.global.apiPayload.code.status.ErrorStatus._CHATLIST_NOT_FOUND;
import static opensource.bravest.global.apiPayload.code.status.ErrorStatus._CHATROOM_NOT_FOUND;
import static opensource.bravest.global.apiPayload.code.status.ErrorStatus._USER_NOT_FOUND;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import opensource.bravest.domain.chatList.entity.ChatList;
import opensource.bravest.domain.chatList.repository.ChatListRepository;
import opensource.bravest.domain.profile.entity.AnonymousProfile;
import opensource.bravest.domain.profile.repository.AnonymousProfileRepository;
import opensource.bravest.domain.room.entity.AnonymousRoom;
import opensource.bravest.domain.room.repository.AnonymousRoomRepository;
import opensource.bravest.global.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatListService {

    private final ChatListRepository chatListRepository;
    private final AnonymousRoomRepository anonymousRoomRepository;
    private final AnonymousProfileRepository anonymousProfileRepository;

    @Transactional
    public ChatListResponse createChatList(ChatListCreateRequest request) {

        Long id = 1L;

        AnonymousRoom room = anonymousRoomRepository.findById(request.getRoomId())
            .orElseThrow(() -> new CustomException(_CHATROOM_NOT_FOUND));

        ChatList chatList = chatListRepository.findById(id)
            .orElseThrow(ChatListNotFoundException::new);

        AnonymousProfile profile = anonymousProfileRepository.findById(request.getRegisteredBy())
            .orElseThrow(() -> new CustomException(_USER_NOT_FOUND));

        ChatList chatList = ChatList.builder()
            .room(room)
            .registeredBy(profile)
            .content(request.getContent())
            .build();

        ChatList savedList = chatListRepository.save(chatList);
        return ChatListResponse.fromEntity(savedList);
    }

    public List<ChatListResponse> getChatListsByRoomId(Long roomId) {
        List<ChatList> chatLists = chatListRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId);
        return chatLists.stream()
            .map(ChatListResponse::fromEntity)
            .collect(Collectors.toList());
    }

    public ChatListResponse getChatListById(Long id) {
        ChatList chatList = chatListRepository.findById(id)
            .orElseThrow(() -> new CustomException(_CHATLIST_NOT_FOUND));
        return ChatListResponse.fromEntity(chatList);
    }

    @Transactional
    public ChatListResponse updateChatList(Long id, ChatListUpdateRequest request) {
        ChatList chatList = chatListRepository.findById(id)
            .orElseThrow(() -> new CustomException(_CHATLIST_NOT_FOUND));

        chatList.updateContent(request.getContent());

        return ChatListResponse.fromEntity(chatList);
    }

    @Transactional
    public void deleteChatList(Long id) {
        if (!chatListRepository.existsById(id)) {
            throw new CustomException(_CHATLIST_NOT_FOUND);
        }
        chatListRepository.deleteById(id);
    }
}