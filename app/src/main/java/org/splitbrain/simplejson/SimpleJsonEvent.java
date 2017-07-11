package org.splitbrain.simplejson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by sadboy on 6/18/17.
 */

public class SimpleJsonEvent {

    private final HashMap<String, SimpleJsonEvent.JsonEventFacet> data;

    /**
     * Constructor
     */
    public SimpleJsonEvent() {
        data = new HashMap<String, SimpleJsonEvent.JsonEventFacet>();
    }

    /**
     * Simplistic json date parser
     *
     * @param date A string in json format
     * @return the parsed date
     * @throws java.text.ParseException if the parsing fails
     */
    public static Date parseDate(String date) throws ParseException {

        Date dt;

        if (date.contains("+")) {
            date = date.substring(0, date.lastIndexOf('+'));
        } else {
            date = date.substring(0, date.lastIndexOf('-'));
        }

        date = date.replaceAll("-", "");
        date = date.replaceAll(":", "");

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        dt = df.parse(date);

        return dt;
    }

    /**
     * Convenience method to get the start_time key as a Date
     *
     * @return null if the field isn't set or can't be parsed
     */
    public Date getStartDate() {
        String date = get("start_time");
        if (date == null) return null;

        try {
            return parseDate(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Getting end_time from the data
     */

    public Date getEndDate() {
        String date = get("end_time");

        if (date == null) {
            return null;
        }

        try {
            return parseDate(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Sets the value of the given key
     *
     * @param key the key
     * @param val the value
     */
    public void set(String key, String val) {
        key = key.toUpperCase();
        SimpleJsonEvent.JsonEventFacet facet;
        if (data.containsKey(key)) {
            facet = data.get(key);
        } else {
            facet = new SimpleJsonEvent.JsonEventFacet();
            data.put(key, facet);
        }

        facet.set(val);
    }

    /**
     * Sets a parameter value of a given key
     *
     * @param key   the key
     * @param param the attribute
     * @param val   the value
     */
    public void set(String key, String param, String val) {
        key = key.toUpperCase();
        param = param.toUpperCase();
        SimpleJsonEvent.JsonEventFacet facet;
        if (data.containsKey(key)) {
            facet = data.get(key);
        } else {
            facet = new SimpleJsonEvent.JsonEventFacet();
            data.put(key, facet);
        }

        facet.set(param, val);
    }

    /**
     * Returns the value of a given key
     *
     * @param key the key
     * @return null if the key doesn't exist
     */
    public String get(String key) {
        key = key.toUpperCase();
        if (data.containsKey(key)) {
            return data.get(key).get();
        } else {
            return null;
        }
    }

    /**
     * Returns a parameter value of a given key
     *
     * @param key   the key
     * @param param the attribute
     * @return null if the key or parameter doesn't exist
     */
    public String get(String key, String param) {
        key = key.toUpperCase();
        param = param.toUpperCase();
        if (data.containsKey(key)) {
            return data.get(key).get(param);
        } else {
            return null;
        }
    }

    private class JsonEventFacet {
        private final HashMap<String, String> extra;
        private String value;

        public JsonEventFacet() {
            extra = new HashMap<String, String>();
        }

        public void set(String val) {
            value = val;
        }

        public String get() {
            return value;
        }

        public void set(String key, String val) {
            key = key.toUpperCase();
            extra.put(key, val);
        }

        public String get(String key) {
            key = key.toUpperCase();
            if (extra.containsKey(key)) {
                return extra.get(key);
            } else {
                return null;
            }
        }
    }
}
