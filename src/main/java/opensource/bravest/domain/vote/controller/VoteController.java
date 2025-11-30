package opensource.bravest.domain.vote.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import opensource.bravest.domain.vote.dto.VoteDto;
import opensource.bravest.domain.vote.entity.Vote;
import opensource.bravest.domain.vote.service.VoteService;
import opensource.bravest.global.apiPayload.ApiResponse;
import opensource.bravest.global.apiPayload.code.status.SuccessStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votes")
public class VoteController {

  private final VoteService voteService;

  @PostMapping
  @Operation(summary = "투표 생성", description = "새로운 투표를 생성합니다.")
  public ApiResponse<VoteDto.VoteResponse> createVote(
      @RequestBody VoteDto.CreateVoteRequest request) {
    Vote vote = voteService.createVote(request);
    // The response DTO needs to be built manually
    VoteDto.VoteResponse responseDto = voteService.getVoteResult(vote.getId());
    return ApiResponse.of(SuccessStatus._CREATED, SuccessStatus._CREATED.getMessage(), responseDto);
  }

  @GetMapping("/{voteId}")
  @Operation(summary = "투표 조회", description = "ID로 특정 투표의 정보를 조회합니다.")
  public ApiResponse<VoteDto.VoteResponse> getVote(@PathVariable Long voteId) {
    VoteDto.VoteResponse responseDto = voteService.getVoteResult(voteId);
    return ApiResponse.of(SuccessStatus._OK, SuccessStatus._OK.getMessage(), responseDto);
  }

  @PostMapping("/{voteId}/cast")
  @Operation(summary = "투표 참여", description = "특정 투표 항목에 투표합니다.")
  public ApiResponse<?> castVote(
      @PathVariable Long voteId, @RequestBody VoteDto.CastVoteRequest request) {
    voteService.castVote(voteId, request);
    return ApiResponse.of(SuccessStatus._OK, SuccessStatus._OK.getMessage(), null);
  }

  @PostMapping("/{voteId}/end")
  @Operation(summary = "투표 종료", description = "특정 투표를 종료합니다.")
  public ApiResponse<?> endVote(@PathVariable Long voteId) {
    voteService.endVote(voteId);
    return ApiResponse.of(SuccessStatus._OK, SuccessStatus._OK.getMessage(), null);
  }

  @GetMapping("/{voteId}/result")
  @Operation(summary = "투표 결과 조회", description = "종료된 투표의 결과를 조회합니다.")
  public ApiResponse<VoteDto.VoteResponse> getVoteResult(@PathVariable Long voteId) {
    VoteDto.VoteResponse responseDto = voteService.getVoteResult(voteId);
    return ApiResponse.of(SuccessStatus._OK, SuccessStatus._OK.getMessage(), responseDto);
  }

  @DeleteMapping("/{voteId}")
  @Operation(summary = "투표 삭제", description = "ID로 특정 투표를 삭제합니다.")
  public ApiResponse<?> deleteVote(@PathVariable Long voteId) {
    voteService.deleteVote(voteId);
    return ApiResponse.of(SuccessStatus._OK, SuccessStatus._OK.getMessage(), null);
  }
}
