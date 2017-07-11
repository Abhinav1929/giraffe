package org.splitbrain.simplejson;

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

    private BufferedReader reader = null;
    public ArrayList<SimpleJsonEvent> listEvent = new ArrayList<SimpleJsonEvent>();

    /**
     * Initializes the parser on the given input stream
     *
     * @param is Stream to the java formatted data to parse
     */
    public SimpleJsonParser(InputStream is) {
        reader = new BufferedReader(new InputStreamReader(is));
    }

    /**
     * Read the next from the stream
     *
     * @return The Event or null if no more events can be found.
     * @throws IOException when reading from the stream fails
     */
    public ArrayList<SimpleJsonEvent> nextEvent() throws IOException {

        StringBuffer buffer = new StringBuffer();
        SimpleJsonEvent event = null;
        Scanner s = new Scanner(reader);

        while (s.hasNext()) {
            buffer.append(s.nextLine());
        }

        String output = buffer.toString();

        try {
            JSONObject conference = new JSONObject(output);
            int i;

            //The sessions are contained in the array present in an object
            JSONArray events = conference.getJSONArray("sessions");

            for (i = 0; i < events.length(); i++) {
                JSONObject eventsJSONObject = events.getJSONObject(i);
                event = new SimpleJsonEvent();

                event.setId(eventsJSONObject.getString("id"));
                event.setTitle(eventsJSONObject.getString("title"));
                event.setLocation(eventsJSONObject.getString("microlocation"));
                event.setDescription(eventsJSONObject.getString("long_abstract"));
                event.setStarts(eventsJSONObject.getString("start_time"));
                event.setEnds(eventsJSONObject.getString("end_time"));
                event.setUrl(eventsJSONObject.getString("signup_url"));

                listEvent.add(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listEvent;
    }
}
