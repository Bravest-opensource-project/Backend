package opensource.bravest.domain.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import opensource.bravest.domain.profile.dto.AnonymousProfileResponse;
import opensource.bravest.domain.profile.dto.CreateAnonymousProfileRequest;
import opensource.bravest.domain.profile.entity.AnonymousProfile;
import opensource.bravest.domain.profile.service.AnonymousProfileService;
import opensource.bravest.global.apiPayload.ApiResponse;
import opensource.bravest.global.apiPayload.code.status.SuccessStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/anonymous-profiles")
public class AnonymousProfileController {

    private final AnonymousProfileService anonymousProfileService;


    @Operation(summary = "익명 프로필 생성", description = "특정 채팅방에 대한 새로운 익명 프로필을 생성합니다.")
    @PostMapping("/rooms/{roomId}")
    public ApiResponse<AnonymousProfileResponse> createAnonymousProfile(
            @PathVariable Long roomId,
            @RequestBody CreateAnonymousProfileRequest request
    ) {
        AnonymousProfile profile = anonymousProfileService.createAnonymousProfile(roomId, request);
        AnonymousProfileResponse response = AnonymousProfileResponse.from(profile);
        return ApiResponse.of(SuccessStatus._CREATED, SuccessStatus._CREATED.getMessage(), response);
    }

    @DeleteMapping("/{profileId}")
    @Operation(summary = "익명 프로필 삭제", description = "ID로 특정 익명 프로필을 삭제합니다.")
    public ApiResponse<?> deleteAnonymousProfile(@PathVariable Long profileId) {
        anonymousProfileService.deleteAnonymousProfile(profileId);
        return ApiResponse.of(SuccessStatus._OK, SuccessStatus._OK.getMessage(), null);
    }
}
