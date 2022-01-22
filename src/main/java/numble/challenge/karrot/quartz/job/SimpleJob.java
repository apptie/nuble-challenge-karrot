package numble.challenge.karrot.quartz.job;

import lombok.extern.slf4j.Slf4j;
import numble.challenge.karrot.member.service.MemberService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public class SimpleJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info("SimpleJob executeInternal - delete member start");
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        MemberService memberService = (MemberService) jobDataMap.get("memberService");
        String email = (String) jobDataMap.get("email");

        memberService.deleteNotVerifyMember(email);
    }
}