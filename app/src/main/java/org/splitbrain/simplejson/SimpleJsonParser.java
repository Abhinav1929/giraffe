package org.splitbrain.simplejson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by sadboy on 6/18/17.
 */

public class SimpleJsonParser {

    private String line = null;
    private String nextline = null;
    private BufferedReader reader = null;

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
    public SimpleJsonEvent nextEvent() throws IOException {
        return null;
    }
}
