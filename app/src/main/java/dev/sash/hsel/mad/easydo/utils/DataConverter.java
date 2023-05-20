package dev.sash.hsel.mad.easydo.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataConverter {

    public static final String DATE_FORMAT = "EEEE, dd. MMMM yyyy HH:mm";

    public static String fromDateToText(long date) {
        return (date == Long.MAX_VALUE) ? "" : new SimpleDateFormat(DATE_FORMAT, Locale.GERMANY).format(new Date(date));
    }

    public static boolean isDateExpired(long date) {
        return (date < new Date().getTime());
    }

    @TypeConverter
    public static String fromContactsList(List<String> contacts_list) {
        if (contacts_list == null || contacts_list.isEmpty()) return "";
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.toJson(contacts_list, type);
    }

    @TypeConverter
    public static List<String> toContactsList(String contacts) {
        if (contacts == null || contacts.isEmpty()) return new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(contacts, type);
    }

}
