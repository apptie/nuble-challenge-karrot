package numble.challenge.karrot.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${mail-send-sender}")
    private String sender;

    @Value("${mail-send-image-path}")
    private String imagePath;

    @Value("${mail-send-link}")
    private String link;

    @Override
    public void mailSend(String email, String uuid) throws MessagingException {

        FileSystemResource image = new FileSystemResource(new File(imagePath));

        StringBuilder sb = new StringBuilder();

        sb.append("<h1>karrot 마켓 이메일 인증</h1>")
                .append("<img src=\"cid:imageTarget\" width=\"100px\" height=\"100px\">")
                .append("<br><hr>")
                .append("<span>만약 karrot 마켓에 가입하신 적이 없다면 이 메일은 무시해주세요.")
                .append("<br><br>")
                .append("<a href=\"")
                .append(link)
                .append(email)
                .append("/")
                .append(uuid)
                .append("\">링크</a>를 클릭하시면 메일 인증이 완료됩니다.");

        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");

        helper.setFrom(sender);
        helper.setTo(email);
        helper.setSubject("karrot 마켓 가입을 위한 메일입니다.");
        helper.setText(sb.toString(), true);
        helper.addInline("imageTarget", image);

        mailSender.send(mail);
    }
}
