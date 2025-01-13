package wad.seoul_nolgoat.web.mail;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.seoul_nolgoat.service.mail.MailService;

@Tag(name = "메일")
@RequiredArgsConstructor
@RequestMapping("/api/mail")
@RestController
public class MailController {

    private final MailService mailService;

    @Operation(summary = "회원 탈퇴를 위한 인증 번호 메일 발송")
    @PostMapping("/withdrawal/verification")
    public ResponseEntity<Void> sendEmailWithdrawalVerificationCode(@AuthenticationPrincipal OAuth2User loginUser) {
        mailService.sendEmailWithdrawalVerificationCode(loginUser.getName());

        return ResponseEntity
                .ok()
                .build();
    }
}
