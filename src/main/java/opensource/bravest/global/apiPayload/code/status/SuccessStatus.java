package opensource.bravest.global.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import opensource.bravest.global.apiPayload.code.BaseCode;
import opensource.bravest.global.apiPayload.code.ReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    _OK(HttpStatus.OK, "COMMON2000", "성공입니다."), _CREATED(HttpStatus.CREATED, "COMMON201", "생성되었습니다."),;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder().isSuccess(true).message(message).code(code).build();
    }

    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder().httpStatus(httpStatus).isSuccess(true).code(code).message(message).build();
    }
}
