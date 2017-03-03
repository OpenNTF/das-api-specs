/*
 * © Copyright IBM Corp. 2017
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package client.calendar.test;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.EventApi;
import io.swagger.client.model.Attendee;
import io.swagger.client.model.Event;
import io.swagger.client.model.EventRequest;
import io.swagger.client.model.EventResponse;
import io.swagger.client.model.ExDateTime;
import io.swagger.client.model.Organizer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MeetingTest {

    private EventApi _eventApi;

    public MeetingTest(String basePath, String user, String password) {
        _eventApi = new EventApi();
        ApiClient client = _eventApi.getApiClient();
        client.setBasePath(basePath);
        client.setUsername(user);
        client.setPassword(password);
    }

    public static void main(String[] args) {

        // IMPORTANT: Change these values to match your test environment:
        //
        // - basePath is the base URL for your Domino server.
        //   The calendar service MUST be installed and enabled
        //   on the server. Be sure to change the protocol to
        //   https if necessary.
        //
        // - folder is mail file database folder.
        //
        // - database is the mail file database name.
        //
        // - user is the user name of the owner of the mail file specified by
        //   /{folder}/{database}.
        //
        // - password is the user's password.
        //
        // - organizerEmail is the email address of the event organizer. This is
        //   usually the internet address of {user}
        //
        // - attendeeEmail is the email address of an invitee.
        //
        String basePath = "http://yourserver.yourorg.com";
        String folder = "mail";
        String database = "database.nsf";
        String user = "First Last";
        String password = "password";
        String organizerEmail = "organizer@yourorg.com";
        String attendeeEmail = "attendee@yourorg.com";

        // Create an instance of the test class
        MeetingTest test = new MeetingTest(basePath, user, password);

        // Create an event
        String uid = test.createMeeting(folder, database, organizerEmail, attendeeEmail);

        if (uid != null) {
            // Read the event
            test.readMeeting(folder, database, uid);

            // Delete the event
            test.deleteMeeting(folder, database, uid);
        }

    }

    /**
     * Creates a meeting on the user's calendar.
     * 
     * @param folder
     * @param database
     * @param organizerEmail
     * @param attendeeEmail
     * @return
     */
    private String createMeeting(String folder, String database, String organizerEmail, String attendeeEmail) {
        String uid = null;
        Event event = new Event();
        event.setSummary("API test");
        event.setLocation("test");
        event.setDescription("test");

        ExDateTime start = new ExDateTime();
        start.setDate("2018-01-01");
        start.setTime("13:00:00");
        start.setUtc(true);
        event.setStart(start);

        ExDateTime end = new ExDateTime();
        end.setDate("2018-01-01");
        end.setTime("14:00:00");
        end.setUtc(true);
        event.setEnd(end);

        // A meeting must have an organizer
        Organizer organizer = new Organizer();
        organizer.setEmail(organizerEmail);
        event.setOrganizer(organizer);

        // A meeting must have at least one attendee
        Attendee attendee = new Attendee();
        attendee.setEmail(attendeeEmail);
        attendee.setRole("req-participant");
        attendee.setStatus("needs-action");
        attendee.setRsvp(true);
        event.getAttendees().add(attendee);

        EventRequest request = new EventRequest();
        request.getEvents().add(event);

        try {

            // Create the event
            System.out.println("Creating an event in " + folder + "/" + database + " ...");
            ApiResponse<Void> result = _eventApi.folderDatabaseApiCalendarEventsPostWithHttpInfo(folder, 
                                            database, request, true);

            // Extract the Location header
            String location = null;
            Map<String, List<String>> headers = result.getHeaders();
            if (headers != null) {
                Set<String> keys = headers.keySet();
                for (String key : keys) {
                    if (key.equalsIgnoreCase("Location")) {
                        location = headers.get(key).get(0);
                        break;
                    }
                }
            }

            // Extract the UID from the location
            if (location != null) {
                // Dump the Location header
                System.out.println("Create event request succeeded.  Location is " + location);

                int index = location.lastIndexOf('/');
                if (index != -1) {
                    uid = location.substring(index + 1, location.length());
                }
            }
            else {
                // Unexpected response
                System.out.println("Create event request succeeded, but the response doesn't include a Location header.");
            }

            System.out.println();
        }
        catch (ApiException e) {
            System.err.println("Exception when calling EventApi#folderDatabaseApiCalendarEventsPostWithHttpInfo");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }

        return uid;
    }

    /**
     * Reads a meeting and dumps it's properties to the console.
     * 
     * @param folder
     * @param database
     * @param uid
     */
    private void readMeeting(String folder, String database, String uid) {
        try {

            // Read the event
            System.out.println("Reading event " + uid + " from " + folder + "/" + database + " ...");
            EventResponse result = _eventApi.folderDatabaseApiCalendarEventsUidGet(folder, database, uid, null);

            // Does the response have events?
            List<Event> events = result.getEvents();
            if (events != null) {

                // Dump the events
                System.out.println("Read event request succeeded.  Response follows ...\n");
                for (int i = 0; i < events.size(); i++) {
                    Event event = events.get(i);
                    System.out.println("Event " + i);
                    System.out.println("  summary: " + event.getSummary());
                    System.out.println("  location: " + event.getLocation());
                    System.out.println("  href: " + event.getHref());

                    String start = event.getStart().getDate();
                    if (event.getStart().getTime() != null) {
                        start += "T" + event.getStart().getTime();
                    }
                    System.out.println("  start: " + start);

                    String end = event.getEnd().getDate();
                    if (event.getEnd().getTime() != null) {
                        end += "T" + event.getEnd().getTime();
                    }
                    System.out.println("  end: " + end);

                    if (event.getOrganizer() == null) {
                        System.err.println("  Event has no organizer!");
                    }
                    else {
                        System.out.println("  organizer: " + event.getOrganizer().getDisplayName());
                    }

                    for (Attendee attendee : event.getAttendees()) {
                        System.out.println("  attendee: " + attendee.getDisplayName() + "; " + attendee.getRole());
                    }

                    System.out.println("  sequence: " + event.getSequence());
                    System.out.println("  description: " + event.getDescription());

                    if (event.getXLotusAppttype() == null || !"3".equals(event.getXLotusAppttype().getData())) {
                        System.err.println("  x-lotus-appttype is missing or not equal to '3'");
                    }
                }
            }
            else {

                // Unexpected response
                System.out.println("Read event request succeeded, but the response doesn't include a list of events.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling EventApi#folderDatabaseApiCalendarEventsUidGet");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes a meeting from the user's calendar.
     * 
     * @param folder
     * @param database
     * @param uid
     */
    private void deleteMeeting(String folder, String database, String uid) {
        try {

            // Delete the event
            System.out.println("Deleting event " + uid + " from " + folder + "/" + database + " ...");
            _eventApi.folderDatabaseApiCalendarEventsUidDelete(folder, database, uid, true);

        }
        catch (ApiException e) {
            System.err.println("Exception when calling EventApi#folderDatabaseApiCalendarEventsUidDelete");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }
    }
}
