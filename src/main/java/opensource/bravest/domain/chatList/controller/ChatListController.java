package opensource.bravest.domain.chatList.controller;

import static opensource.bravest.domain.chatList.dto.ChatListDto.ChatListCreateRequest;
import static opensource.bravest.domain.chatList.dto.ChatListDto.ChatListResponse;
import static opensource.bravest.domain.chatList.dto.ChatListDto.ChatListUpdateRequest;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import opensource.bravest.domain.chatList.service.ChatListService;
import opensource.bravest.global.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatlists")
@RequiredArgsConstructor
public class ChatListController {

    private final ChatListService chatListService;

    @PostMapping
    public ApiResponse<ChatListResponse> createChatList(@Valid @RequestBody ChatListCreateRequest request) {
        ChatListResponse response = chatListService.createChatList(request);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/room/{roomId}")
    public ApiResponse<List<ChatListResponse>> getChatListsByRoomId(@PathVariable Long roomId) {
        List<ChatListResponse> response = chatListService.getChatListsByRoomId(roomId);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<ChatListResponse> getChatListById(@PathVariable Long id) {
        ChatListResponse response = chatListService.getChatListById(id);
        return ApiResponse.onSuccess(response);
    }

    @PutMapping("/{id}")
    public ApiResponse<ChatListResponse> updateChatList(@PathVariable Long id,
            @Valid @RequestBody ChatListUpdateRequest request) {
        ChatListResponse response = chatListService.updateChatList(id, request);
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteChatList(@PathVariable Long id) {
        chatListService.deleteChatList(id);
        return ApiResponse.onSuccess(null);
    }
}
