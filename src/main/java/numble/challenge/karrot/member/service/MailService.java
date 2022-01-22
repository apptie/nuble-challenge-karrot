package numble.challenge.karrot.member.service;

import javax.mail.MessagingException;

public interface MailService {
    void mailSend(String email, String uuid) throws MessagingException;
}
