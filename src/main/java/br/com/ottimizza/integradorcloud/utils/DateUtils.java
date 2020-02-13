package br.com.ottimizza.integradorcloud.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    public static Date toDate(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDate ld) {
        if (ld == null) {
            return null;
        }
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(Date dt) {
        if (dt == null) {
            return null;
        }
        return dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date dt) {
        if (dt == null) {
            return null;
        }
        return dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

}