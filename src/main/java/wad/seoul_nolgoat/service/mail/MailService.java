package wad.seoul_nolgoat.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import wad.seoul_nolgoat.auth.service.RedisWithdrawalCodeService;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApiException;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import static wad.seoul_nolgoat.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class MailService {

    private final UserRepository userRepository;
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final RedisWithdrawalCodeService redisWithdrawalCodeService;

    public void sendEmailWithdrawalCode(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        String email = user.getEmail();
        String nickname = user.getNickname();

        // 이메일 내용 생성
        Context context = new Context();
        context.setVariable("nickname", nickname);
        context.setVariable("verificationCode", generateVerificationCode(loginId));
        String text = templateEngine.process("mail-withdrawal.html", context);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(new InternetAddress("llyyoo93@gmail.com", "NolGoat", "UTF-8"));
            helper.setTo(email);
            helper.setSubject("NolGoat 회원 탈퇴 인증 번호 안내");
            helper.setText(text, true);

            // 메일 템플릿에서 사용할 이미지를 cid로 등록하여 첨부합니다.
            helper.addInline("logo-icon", new ClassPathResource("static/images/logo-icon.png"));

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new ApiException(MAIL_SEND_FAILED, e);
        } catch (UnsupportedEncodingException e) {
            throw new ApiException(MAIL_SENDER_ENCODING_FAILED, e);
        }
    }

    private String generateVerificationCode(String loginId) {
        Random random = new Random();
        String verificationCode = String.format("%04d", random.nextInt(10000)); // 4자리 숫자 생성 (0000~9999)
        redisWithdrawalCodeService.saveCode(loginId, verificationCode);

        return verificationCode;
    }
}
