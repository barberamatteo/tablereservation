package it.matteobarbera.tablereservation.model.preferences;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserPreferences {
    public final Long DEFAULT_LEAVE_TIME_MINUTES_OFFSET = 90L;

}
