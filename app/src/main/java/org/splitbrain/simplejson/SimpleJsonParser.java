package org.splitbrain.simplejson;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.splitbrain.simpleical.SimpleIcalEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by sadboy on 6/18/17.
 */

public class SimpleJsonParser {

    private String output;
    public ArrayList<SimpleJsonEvent> listEvent = new ArrayList<SimpleJsonEvent>();


    public SimpleJsonParser(String output) {
        this.output = output;
    }

    /**
     * Read the next from the stream
     *
     * @return The Event or null if no more events can be found.
     * @throws IOException when reading from the stream fails
     */
    public ArrayList<SimpleJsonEvent> nextEvent() throws IOException {

        SimpleJsonEvent event;
        Log.e("output", output);
        try {
            JSONObject conference = new JSONObject(output);
            int i, j;

            //The sessions are contained in the array present in an object
            JSONArray events = conference.getJSONArray("sessions");

            for (i = 0; i < events.length(); i++) {
                JSONObject eventsJSONObject = events.getJSONObject(i);
                event = new SimpleJsonEvent();

                event.setId(eventsJSONObject.getString("id"));
                event.setTitle(eventsJSONObject.getString("title"));
                JSONObject microlocation = eventsJSONObject.getJSONObject("microlocation");
                String location = microlocation.getString("name");
                event.setLocation(location);
                String description = eventsJSONObject.getString("long_abstract");
                event.setDescription(android.text.Html.fromHtml(description).toString());
                event.setStarts(eventsJSONObject.getString("start_time"));
                event.setEnds(eventsJSONObject.getString("end_time"));
                event.setUrl(eventsJSONObject.getString("signup_url"));

                JSONArray speakers = eventsJSONObject.getJSONArray("speakers");
                String sp = "";

                for (j = 0; j < speakers.length(); j++) {
                    JSONObject ob = speakers.getJSONObject(j);
                    if (j == 0) {
                        sp += ob.getString("name");
                    } else {
                        sp += ", " + ob.getString("name");
                    }
                }

                event.setSpeaker(sp);
                listEvent.add(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listEvent;
    }
}
