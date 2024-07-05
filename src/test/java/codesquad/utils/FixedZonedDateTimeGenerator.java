package codesquad.utils;

import codesquad.utils.time.ZonedDateTimeGenerator;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class FixedZonedDateTimeGenerator implements ZonedDateTimeGenerator {

    private final ZonedDateTime zonedDateTime;

    public FixedZonedDateTimeGenerator(int year, int month, int dayOfMonth, int hour, int minute, int second,
                                       int nonoOfSecond, ZoneId zoneId) {
        zonedDateTime = ZonedDateTime.of(year, month, dayOfMonth, hour, minute, second, nonoOfSecond, zoneId);
    }

    @Override
    public ZonedDateTime now() {
        return zonedDateTime;
    }
    
}
