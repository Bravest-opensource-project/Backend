package opensource.bravest.domain.vote.repository;

import opensource.bravest.domain.vote.entity.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {
}
