package opensource.bravest.domain.profile.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAnonymousProfileRequest {
  private Long realUserId;
  private String anonymousName;
}
