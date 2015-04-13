package com.taesua.admeet.admeet;

/**
 * Created by Hector on 09/04/2015.
 */


import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class Ids {
    public static final String WEB_CLIENT_ID = "905819273656-i2c3hm5tp0ltmcimcqciem03ll0prtev.apps.googleusercontent.com";
    /**
     * The audience is defined by the web client id, not the Android client id.
     */
    public static final String AUDIENCE = "server:client_id:" + WEB_CLIENT_ID;

    /**
     * Class instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

    /**
     * Class instance of the HTTP transport.
     */
    public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

    //905819273656-rhcn7btti5vf2nitm7sa51euobidugv9.apps.googleusercontent.com CLIENT ANDROID ID UDACITY
    //905819273656-dugivi3m7nu9gchsr3fe5cal00n572do.apps.googleusercontent.com CLIENT ANDROID ID ADMEET
}


