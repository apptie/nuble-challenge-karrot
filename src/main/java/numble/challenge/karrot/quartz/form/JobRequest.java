package numble.challenge.karrot.quartz.form;

import lombok.*;
import org.quartz.JobDataMap;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JobRequest {

    private int repeatCount = 1;

    private String jobName;

    private LocalDateTime startDateAt;
    private long repeatIntervalInSeconds;
    private JobDataMap jobDataMap;
}