package numble.challenge.karrot.common.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ChronoString {
    public static String getChronoString(LocalDateTime createdDate) {
        LocalDateTime now = LocalDateTime.now();

        long years = ChronoUnit.MONTHS.between(createdDate, now);
        long months = ChronoUnit.MONTHS.between(createdDate, now);
        long days = ChronoUnit.DAYS.between(createdDate, now);
        long hours = ChronoUnit.HOURS.between(createdDate, now);
        long minutes = ChronoUnit.MINUTES.between(createdDate, now);

        StringBuilder sb = new StringBuilder();
        if (years > 0) {
            sb.append(years).append("년");
        }
        else if (months > 0) {
            sb.append(months).append("달");
        }
        else if (days > 0) {
            sb.append(days).append("일");
        }
        else if (hours > 0) {
            sb.append(hours).append("시간");
        }
        else {
            sb.append(minutes).append("분");
        }

        sb.append(" 전");

        return sb.toString();
    }
}
