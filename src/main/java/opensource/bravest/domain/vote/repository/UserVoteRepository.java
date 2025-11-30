package opensource.bravest.domain.vote.repository;

import java.util.Optional;
import opensource.bravest.domain.profile.entity.AnonymousProfile;
import opensource.bravest.domain.vote.entity.UserVote;
import opensource.bravest.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVoteRepository extends JpaRepository<UserVote, Long> {
    Optional<UserVote> findByVoteAndVoter(Vote vote, AnonymousProfile voter);
}
