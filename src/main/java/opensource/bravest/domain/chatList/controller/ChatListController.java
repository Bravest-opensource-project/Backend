package opensource.bravest.domain.chatList.controller;

import static opensource.bravest.domain.chatList.dto.ChatListDto.ChatListResponse;
import static opensource.bravest.domain.chatList.dto.ChatListDto.ChatListUpdateRequest;
import static opensource.bravest.domain.chatList.dto.ChatListDto.ChatListCreateRequest;

import opensource.bravest.domain.chatList.service.ChatListService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chatlists")
@RequiredArgsConstructor
public class ChatListController {

    private final ChatListService chatListService;

    @PostMapping
    public ResponseEntity<ChatListResponse> createChatList(@Valid @RequestBody ChatListCreateRequest request) {
        ChatListResponse response = chatListService.createChatList(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // 201 Created
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ChatListResponse>> getChatListsByRoomId(@PathVariable Long roomId) {
        List<ChatListResponse> response = chatListService.getChatListsByRoomId(roomId);
        return ResponseEntity.ok(response); // 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatListResponse> getChatListById(@PathVariable Long id) {
        ChatListResponse response = chatListService.getChatListById(id);
        return ResponseEntity.ok(response); // 200 OK
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatListResponse> updateChatList(@PathVariable Long id,
                                                           @Valid @RequestBody ChatListUpdateRequest request) {
        ChatListResponse response = chatListService.updateChatList(id, request);
        return ResponseEntity.ok(response); // 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatList(@PathVariable Long id) {
        chatListService.deleteChatList(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }
}