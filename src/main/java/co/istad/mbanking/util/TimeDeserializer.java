package co.istad.mbanking.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Custom deserializer to convert time strings (HH:mm:ss) to LocalTime objects
 */
public class TimeDeserializer extends JsonDeserializer<LocalTime> {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String timeStr = p.getText();
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }

        try {
            return LocalTime.parse(timeStr, TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IOException("Invalid time format. Expected format: HH:mm:ss (e.g., 08:00:00)", e);
        }
    }
}
