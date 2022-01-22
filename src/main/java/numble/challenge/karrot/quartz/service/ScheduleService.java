package numble.challenge.karrot.quartz.service;

import numble.challenge.karrot.quartz.form.JobRequest;
import org.quartz.Job;
import org.quartz.JobKey;

public interface ScheduleService {
    boolean isJobRunning(JobKey jobKey);

    boolean isJobExists(JobKey jobKey);

    boolean addJob(JobRequest jobRequest, Class<? extends Job> jobClass);

    boolean deleteJob(JobKey jobKey);
}