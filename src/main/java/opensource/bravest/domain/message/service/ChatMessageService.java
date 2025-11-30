package opensource.bravest.domain.message.service;

import static opensource.bravest.domain.message.dto.MessageDto.MessageRequest;
import static opensource.bravest.domain.message.dto.MessageDto.MessageResponse;
import static opensource.bravest.global.apiPayload.code.status.ErrorStatus._CHATROOM_NOT_FOUND;
import static opensource.bravest.global.apiPayload.code.status.ErrorStatus._USER_NOT_FOUND;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import opensource.bravest.domain.message.entity.ChatMessage;
import opensource.bravest.domain.message.repository.ChatMessageRepository;
import opensource.bravest.domain.profile.entity.AnonymousProfile;
import opensource.bravest.domain.profile.repository.AnonymousProfileRepository;
import opensource.bravest.domain.room.entity.AnonymousRoom;
import opensource.bravest.domain.room.repository.AnonymousRoomRepository;
import opensource.bravest.global.exception.CustomException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final AnonymousProfileRepository memberRepository;
    private final AnonymousRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 메시지 전송
    public MessageResponse send(MessageRequest request, Long id) {
        AnonymousProfile sender = memberRepository.findById(id).orElseThrow(() -> new CustomException(_USER_NOT_FOUND));

        AnonymousRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new CustomException(_CHATROOM_NOT_FOUND));

        ChatMessage chatMessage = ChatMessage.builder().room(chatRoom).sender(sender).content(request.getContent())
                .build();

        chatMessageRepository.save(chatMessage);

        return MessageResponse.from(chatMessage);
    }

    @Transactional
    public void readMessages(Long chatRoomId, Long memberId) {
        AnonymousRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(_CHATROOM_NOT_FOUND));

        // if (!Objects.equals(chatRoom.getMember1().getId(), memberId) &&
        // !Objects.equals(chatRoom.getMember2().getId(),
        // memberId)) {
        // throw new BaseException(ChatExceptionType.CHAT_ROOM_ACCESS_DENIED);
        // }
        // messageReceiptRepository.bulkUpdateStatusToRead(chatRoomId, memberId);
    }
}
