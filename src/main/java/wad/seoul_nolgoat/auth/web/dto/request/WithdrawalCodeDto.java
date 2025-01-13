package wad.seoul_nolgoat.auth.web.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class WithdrawalCodeDto {

    private final String inputCode;

    @JsonCreator
    public WithdrawalCodeDto(String inputCode) {
        this.inputCode = inputCode;
    }
}
