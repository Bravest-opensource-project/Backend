package opensource.bravest.domain.vote.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import opensource.bravest.domain.profile.entity.AnonymousProfile;
import opensource.bravest.domain.profile.repository.AnonymousProfileRepository;
import opensource.bravest.domain.room.entity.AnonymousRoom;
import opensource.bravest.domain.room.repository.AnonymousRoomRepository;
import opensource.bravest.domain.vote.dto.VoteDto;
import opensource.bravest.domain.vote.entity.UserVote;
import opensource.bravest.domain.vote.entity.Vote;
import opensource.bravest.domain.vote.entity.VoteOption;
import opensource.bravest.domain.vote.repository.UserVoteRepository;
import opensource.bravest.domain.vote.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserVoteRepository userVoteRepository;
    private final AnonymousRoomRepository anonymousRoomRepository;
    private final AnonymousProfileRepository anonymousProfileRepository;

    @Transactional
    public Vote createVote(VoteDto.CreateVoteRequest request) {
        AnonymousRoom room = anonymousRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Vote vote = Vote.builder().room(room).title(room.getTitle()).isActive(true).createdAt(LocalDateTime.now())
                .build();

        List<VoteOption> options = request.getMessages().stream()
                .map(message -> VoteOption.builder().vote(vote).messageContent(message).voteCount(0).build())
                .collect(Collectors.toList());

        vote.getOptions().addAll(options);

        return voteRepository.save(vote);
    }

    @Transactional
    public void castVote(Long voteId, VoteDto.CastVoteRequest request) {
        Vote vote = voteRepository.findById(voteId).orElseThrow(() -> new RuntimeException("Vote not found"));
        if (!vote.isActive()) {
            throw new RuntimeException("Vote is not active");
        }

        AnonymousProfile voter = anonymousProfileRepository.findById(request.getAnonymousProfileId())
                .orElseThrow(() -> new RuntimeException("AnonymousProfile not found"));

        if (userVoteRepository.findByVoteAndVoter(vote, voter).isPresent()) {
            throw new RuntimeException("User has already voted");
        }

        VoteOption voteOption = vote.getOptions().stream()
                .filter(option -> option.getId().equals(request.getVoteOptionId())).findFirst()
                .orElseThrow(() -> new RuntimeException("VoteOption not found"));

        voteOption.incrementVoteCount();

        UserVote userVote = UserVote.builder().vote(vote).voteOption(voteOption).voter(voter).build();
        userVoteRepository.save(userVote);
    }

    @Transactional
    public void endVote(Long voteId) {
        Vote vote = voteRepository.findById(voteId).orElseThrow(() -> new RuntimeException("Vote not found"));
        vote.endVote();
    }

    public VoteDto.VoteResponse getVoteResult(Long voteId) {
        Vote vote = voteRepository.findById(voteId).orElseThrow(() -> new RuntimeException("Vote not found"));

        return buildVoteResponse(vote);
    }

    @Transactional
    public void deleteVote(Long voteId) {
        if (!voteRepository.existsById(voteId)) {
            throw new RuntimeException("Vote not found");
        }
        voteRepository.deleteById(voteId);
    }

    private VoteDto.VoteResponse buildVoteResponse(Vote vote) {
        List<VoteDto.VoteOptionResponse> optionResponses = vote.getOptions().stream()
                .map(option -> VoteDto.VoteOptionResponse.builder().id(option.getId())
                        .messageContent(option.getMessageContent()).voteCount(option.getVoteCount()).build())
                .collect(Collectors.toList());

        return VoteDto.VoteResponse.builder().id(vote.getId()).title(vote.getTitle()).isActive(vote.isActive())
                .createdAt(vote.getCreatedAt()).options(optionResponses).build();
    }
}
