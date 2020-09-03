package capstone.backend.api.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@AllArgsConstructor
public class DateUtils {
    public static final String PATTERN_ddMMyyyy = "dd/MM/yyyy";

    public static Date stringToDate(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(dateStr);
    }
}
