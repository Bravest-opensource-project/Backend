package opensource.bravest.domain.profile.dto;

import lombok.Builder;
import lombok.Getter;
import opensource.bravest.domain.profile.entity.AnonymousProfile;

@Getter
@Builder
public class AnonymousProfileResponse {
    private Long id;
    private Long roomId;
    private String nickname;
    // 필요한 필드만

    public static AnonymousProfileResponse from(AnonymousProfile profile) {
        return AnonymousProfileResponse.builder()
                .id(profile.getId())
                .roomId(profile.getRoom().getId())
                .nickname(profile.getAnonymousName())
                .build();
    }
}
