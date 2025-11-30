package opensource.bravest.domain.message.controller;

import static opensource.bravest.domain.message.dto.MessageDto.MessageRequest;
import static opensource.bravest.domain.message.dto.MessageDto.MessageResponse;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opensource.bravest.domain.message.service.ChatMessageService;
import opensource.bravest.global.apiPayload.ApiResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {
  private final ChatMessageService chatMessageService;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/send")
  @SendTo("/subs/chat-rooms")
  public void receiveMessage(MessageRequest request, Principal principal) {
    Long id = Long.parseLong(principal.getName());
    MessageResponse response = chatMessageService.send(request, id);

    // 특정 채팅방 구독자들에게 메시지 전송
    messagingTemplate.convertAndSend(
        "/subs/chat-rooms/" + request.getChatRoomId(), ApiResponse.onSuccess(response));
  }
}
