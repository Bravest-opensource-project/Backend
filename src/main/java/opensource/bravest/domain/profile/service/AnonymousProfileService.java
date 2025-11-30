package opensource.bravest.domain.profile.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import opensource.bravest.domain.profile.dto.CreateAnonymousProfileRequest;
import opensource.bravest.domain.profile.entity.AnonymousProfile;
import opensource.bravest.domain.profile.repository.AnonymousProfileRepository;
import opensource.bravest.domain.room.entity.AnonymousRoom;
import opensource.bravest.domain.room.repository.AnonymousRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnonymousProfileService {

    private final AnonymousProfileRepository anonymousProfileRepository;
    private final AnonymousRoomRepository anonymousRoomRepository;

    @Transactional
    public AnonymousProfile createAnonymousProfile(Long roomId, CreateAnonymousProfileRequest request) {
        AnonymousRoom room = anonymousRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("방을 찾을 수 없음.뿡"));

        // 중복 프로필 체크
        Optional<AnonymousProfile> existingProfile = anonymousProfileRepository.findByRoomAndRealUserId(room,
                request.getRealUserId());
        if (existingProfile.isPresent()) {
            throw new RuntimeException("이미 방에 존재하는 유저임. 다른걸로 접속하셈.");
        }

        AnonymousProfile newProfile = AnonymousProfile.builder().room(room).realUserId(request.getRealUserId())
                .anonymousName(request.getAnonymousName()).build();

        return anonymousProfileRepository.save(newProfile);
    }

    @Transactional
    public void deleteAnonymousProfile(Long profileId) {
        if (!anonymousProfileRepository.existsById(profileId)) {
            throw new RuntimeException("없는 사용자임. 너~ 누구야!");
        }
        anonymousProfileRepository.deleteById(profileId);
    }
}
