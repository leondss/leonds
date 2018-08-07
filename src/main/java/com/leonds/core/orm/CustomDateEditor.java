package com.leonds.core.orm;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Leon
 */
public class CustomDateEditor extends PropertyEditorSupport {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public CustomDateEditor() {
    }

    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.trim().length() == 0) {
            this.setValue(null);
        }

        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
            this.setValue(dateTimeFormat.parse(text));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Could not parse date: " + e.getMessage(), e);
        }
    }

    public String getAsText() {
        Date date = (Date) this.getValue();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
        return date == null ? "" : dateTimeFormat.format(date);
    }
}
