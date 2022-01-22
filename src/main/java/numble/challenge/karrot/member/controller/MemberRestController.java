package numble.challenge.karrot.member.controller;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.common.exception.MailSendErrorException;
import numble.challenge.karrot.member.service.MailService;
import numble.challenge.karrot.member.service.MemberService;
import numble.challenge.karrot.quartz.form.JobRequest;
import numble.challenge.karrot.quartz.job.SimpleJob;
import numble.challenge.karrot.quartz.service.ScheduleService;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;
    private final MailService mailService;
    private final ScheduleService scheduleService;

    @PostMapping("/send-mail")
    public String sendMail(@RequestBody Map<String, String> emailMap) throws MessagingException {
        String email = emailMap.get("email");

        try {
            mailService.mailSend(email, memberService.getUUID(email));

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("memberService", memberService);
            jobDataMap.put("email", email);

            JobRequest jobRequest = JobRequest.builder()
                    .jobDataMap(jobDataMap)
                    .jobName("mail delete target : " + email)
                    .startDateAt(LocalDateTime.now().plusMinutes(30))
                    .build();

            JobKey jobKey = new JobKey(jobRequest.getJobName(), "DEFAULT");

            if (!scheduleService.isJobExists(jobKey)) {
                scheduleService.addJob(jobRequest, SimpleJob.class);
            } else {
                throw new MailSendErrorException();
            }
        } catch (Exception e) {
            throw new MailSendErrorException();
        }

        return "ok";
    }
}
