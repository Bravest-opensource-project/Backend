package opensource.bravest.domain.message.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opensource.bravest.domain.message.service.ChatMessageService;
import opensource.bravest.global.apiPayload.ApiResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;

import static opensource.bravest.domain.message.dto.MessageDto.MessageRequest;
import static opensource.bravest.domain.message.dto.MessageDto.MessageResponse;
import static opensource.bravest.domain.message.dto.MessageDto.ChatReadRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(MessageRequest request, Principal principal) {
        Long id = Long.parseLong(principal.getName());
        MessageResponse response = chatMessageService.send(request, id);

        // 특정 채팅방 구독자들에게 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat-room/" + request.getChatRoomId(), ApiResponse.onSuccess(response));
    }

    @MessageMapping("/chat/read")
    public void readMessage(ChatReadRequest request, Long memberId) {
        chatMessageService.readMessages(request.getChatRoomId(), memberId);
    }

    /**
     * 채팅 테스트용 페이지
     */
    @GetMapping("/chat-test")
    public String chatTestPage() {
        return "chat-test";
    }
}
