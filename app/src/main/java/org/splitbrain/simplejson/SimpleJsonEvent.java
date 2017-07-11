package org.splitbrain.simplejson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sadboy on 6/18/17.
 */

public class SimpleJsonEvent {

    public String id;
    public String title;
    public String location;
    public String description;
    public Date starts;
    public Date ends;
    public String url;

    /**
     * Constructor
     */
    public SimpleJsonEvent() {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        try {
            this.starts = parseDate(starts);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        try {
            this.ends = parseDate(ends);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
