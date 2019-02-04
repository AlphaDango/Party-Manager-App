package lxnkn.bearoundwithparty;

import android.arch.persistence.room.TypeConverter;

import java.sql.Date;
import java.sql.Time;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Time fromTimeString(String value) {
        return value == null ? null : Time.valueOf(value);
    }

    @TypeConverter
    public static String dateToTimeString(Time time) {
        return time == null ? null : String.valueOf(time);
    }
}
