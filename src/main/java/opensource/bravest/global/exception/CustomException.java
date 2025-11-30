package opensource.bravest.global.exception;

import lombok.Getter;
import opensource.bravest.global.apiPayload.code.BaseErrorCode;

/** 서비스/도메인 레이어에서 표준화된 에러코드를 던지기 위한 예외 */
@Getter
public class CustomException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public CustomException(BaseErrorCode errorCode) {
        super(errorCode.getReason().getMessage());
        this.errorCode = errorCode;
    }
}
