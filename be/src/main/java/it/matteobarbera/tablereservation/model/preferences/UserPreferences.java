package it.matteobarbera.tablereservation.model.preferences;

import org.springframework.stereotype.Component;

@Component
public class UserPreferences {
    public Long DEFAULT_LEAVE_TIME_MINUTES_OFFSET = 90L;
    public Boolean PREFER_SIMPLETABLE_FOR_SINGLE_TABLE_RESERVATIONS = true;
}
