/* Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taesua.admeet.admeet;

import android.content.Context;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.io.IOException;

import conference.Conference;
import conference.model.ConferenceCollection;
import conference.model.ConferenceForm;
import conference.model.ConferenceQueryForm;
import conference.model.ProfileForm;


/**
 * A utility class for communication with the Cloud Endpoint.
 */
public class ConferenceUtils {

    private final static String TAG = "ConferenceUtils";
    private static conference.Conference sApiServiceHandler;

    public static void build(Context context, String email) {
        sApiServiceHandler = buildServiceHandler(context, email);
    }


    /*
    /**
     * Returns a list of {@link DecoratedConference}s.
     * This list includes information about what {@link conference.model.Conference}s
     * user has registered for.
     *
     * @return
     * @throws ConferenceException
     * @see <code>getProfile</code>
     */

    public static Conference.QueryConferences getEventos(ConferenceQueryForm conferenceQueryForm) throws IOException {
        return sApiServiceHandler.queryConferences(conferenceQueryForm);
    }

    public static Conference.GetConferencesCreated getEventosMios() throws IOException {
        return sApiServiceHandler.getConferencesCreated();
    }

    public static Conference.GetConferencesToAttend getEventosAsisto() throws IOException {
        return sApiServiceHandler.getConferencesToAttend();
    }

    public static Conference.RegisterForConference registrarseEvento(String websafe) throws IOException {
        return sApiServiceHandler.registerForConference(websafe);
    }

    public static Conference.UnregisterFromConference desregistrarseEvento(String websafe) throws IOException {
        return sApiServiceHandler.unregisterFromConference(websafe);
    }

    public static Conference.GetProfile getProfile() throws IOException {
        return sApiServiceHandler.getProfile();
    }

    public static Conference.SaveProfile saveProfile(ProfileForm form) throws IOException {
        return sApiServiceHandler.saveProfile(form);
    }

    public static Conference.CreateConference crearEvento(ConferenceForm form) throws IOException {
        return sApiServiceHandler.createConference(form);
    }
    /*
    public static List<DecoratedConference> getConferences()
            throws ConferenceException, IOException {
        if (null == sApiServiceHandler) {
            Log.e(TAG, "getConferences(): no service handler was built");
            throw new ConferenceException();
        }

        conference.Conference.QueryConferences
                queryConferences = sApiServiceHandler.queryConferences(null);
        conference.model.ConferenceCollection conferenceCollection = queryConferences.execute();

        if (conferenceCollection != null && conferenceCollection.getItems() != null) {
            List<Conference> conferences = conferenceCollection.getItems();
            List<DecoratedConference> decoratedList = null;
            if (null == conferences || conferences.isEmpty()) {
                return decoratedList;
            }
            decoratedList = new ArrayList<DecoratedConference>();
            Profile profile = getProfile();
            List<String> registeredConfKeys = null;
            if (null != profile) {
                registeredConfKeys = profile.getConferenceKeysToAttend();
            }
            if (null == registeredConfKeys) {
                registeredConfKeys = new ArrayList<String>();
            }
            for (Conference conference : conferences) {
                DecoratedConference decorated = new DecoratedConference(conference,
                        registeredConfKeys.contains(conference.getWebsafeKey()));
                decoratedList.add(decorated);
            }
            return decoratedList;
        }
        return null;
    }
*/
    /**
     * Registers user for a {@link conference.model.Conference}
     *
     * @param conference
     * @return
     * @throws ConferenceException
     */

    /*
    public static boolean registerForConference(Conference conference)
            throws ConferenceException, IOException {
        if (null == sApiServiceHandler) {
            Log.e(TAG, "registerForConference(): no service handler was built");
            throw new ConferenceException();
        }

        conference.Conference.RegisterForConference
                registerForConference = sApiServiceHandler.registerForConference(
                conference.getWebsafeKey());
        WrappedBoolean result = registerForConference.execute();
        return result.getResult();
    }
*/
    /**
     * Unregisters user from a {@link conference.model.Conference}.
     *
     * @param conference
     * @return
     * @throws ConferenceException
     */

    /*
    public static boolean unregisterFromConference(Conference conference)
            throws ConferenceException, IOException {
        if (null == sApiServiceHandler) {
            Log.e(TAG, "unregisterFromConference(): no service handler was built");
            throw new ConferenceException();
        }

        conference.Conference.UnregisterFromConference
                unregisterFromConference = sApiServiceHandler.unregisterFromConference(
                conference.getWebsafeKey());
        WrappedBoolean result = unregisterFromConference.execute();
        return result.getResult();
    }

    */

    /**
     * Returns the user {@link conference.model.Profile}. Can
     * be used to find out what conferences user is registered for.
     *
     * @return
     * @throws ConferenceException
     */

    /*
    public static Profile getProfile() throws ConferenceException, IOException {
        if (null == sApiServiceHandler) {
            Log.e(TAG, "getProfile(): no service handler was built");
            throw new ConferenceException();
        }

        conference.Conference.GetProfile getProfile =
                sApiServiceHandler.getProfile();
        return getProfile.execute();
    }
*/
    /**
     * Build and returns an instance of {@link conference.Conference}
     *
     * @param context
     * @param email
     * @return
     */


    public static conference.Conference buildServiceHandler(
            Context context, String email) {
        GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(
                context, Ids.AUDIENCE);
        credential.setSelectedAccountName(email);

        conference.Conference.Builder builder
                = new conference.Conference.Builder(
                Ids.HTTP_TRANSPORT,
                Ids.JSON_FACTORY, credential);
        builder.setApplicationName("AdMeet");
        return builder.build();
    }
}
