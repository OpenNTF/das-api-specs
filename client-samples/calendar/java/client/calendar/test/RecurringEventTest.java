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
import io.swagger.client.api.EventInstanceApi;
import io.swagger.client.api.InstanceListApi;
import io.swagger.client.model.Event;
import io.swagger.client.model.EventRequest;
import io.swagger.client.model.EventResponse;
import io.swagger.client.model.ExDateTime;
import io.swagger.client.model.Instance;
import io.swagger.client.model.InstancesResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecurringEventTest {

    private EventApi _eventApi;
    private InstanceListApi _instanceListApi;
    private EventInstanceApi _eventInstanceApi;

    public RecurringEventTest(String basePath, String user, String password) {
        // Get the API instance and set the base path
        // of the target Domino server
        _eventApi = new EventApi();
        ApiClient client = _eventApi.getApiClient();
        client.setBasePath(basePath);
        client.setUsername(user);
        client.setPassword(password);

        _instanceListApi = new InstanceListApi();
        _eventInstanceApi = new EventInstanceApi();
    }

    public static void main(String[] args) {

        // IMPORTANT: Change these values to match your test environment:
        //
        // - basePath is the base URL for your Domino server.
        //   The calendar service MUST be installed and enabled
        //   on the server. Be sure to change the protocol to
        //   https if necessary.
        //
        // - folder is the mail file database folder.
        //
        // - database is the mail file database name.
        //
        // - user is the user name of the owner of the mail file specified by
        //   /{folder}/{database}.
        //
        // - password is the user's password.
        //
        String basePath = "http://yourserver.yourorg.com";
        String folder = "mail";
        String database = "database.nsf";
        String user = "First Last";
        String password = "password";

        // Create an instance of the test class
        RecurringEventTest test = new RecurringEventTest(basePath, user, password);

        // Create an event
        String uid = test.createRecurringEvent(folder, database);

        if (uid != null) {
            // Read the event
            test.readRecurringEvent(folder, database, uid);

            // Read the instances
            List<Instance> instances = test.readInstances(folder, database, uid);

            // Are there three instances?
            if (instances.size() == 3) {
                String recurrenceId = instances.get(1).getRecurrenceId();

                // Read one instance
                test.readEventInstance(folder, database, uid, recurrenceId);

                // Delete one instance
                test.deleteEventInstance(folder, database, uid, recurrenceId);

                // Read the instances again
                instances = test.readInstances(folder, database, uid);
                if (instances.size() != 2) {
                    System.err.println("Unexpected number of instances");
                }
            }
            else {
                System.err.println("Unexpected number of instances");
            }

            // Delete the event
            test.deleteRecurringEvent(folder, database, uid);
        }

    }

    /**
     * Creates a recurring event.
     * 
     * @param folder
     * @param database
     * @return
     */
    private String createRecurringEvent(String folder, String database) {
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

        // A recurring event must have a recurrence rule
        event.setRecurrenceRule("FREQ=WEEKLY;COUNT=3;BYDAY=MO");

        EventRequest request = new EventRequest();
        request.getEvents().add(event);

        try {

            // Create an event
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
     * Reads a recurring event and dumps the results to the console.
     * 
     * @param folder
     * @param database
     * @param uid
     * @return
     */
    private Event readRecurringEvent(String folder, String database, String uid) {
        Event returnEvent = null;

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
                    if (i == 0) {
                        returnEvent = event;
                    }

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

                    System.out.println("  recurrenceRule: " + event.getRecurrenceRule());
                    System.out.println("  sequence: " + event.getSequence());
                    System.out.println("  description: " + event.getDescription());
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

        return returnEvent;
    }

    /**
     * Deletes a recurring event.
     * 
     * @param folder
     * @param database
     * @param uid
     */
    private void deleteRecurringEvent(String folder, String database, String uid) {
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

    /**
     * Reads the list of instances in a recurring event.
     * 
     * @param folder
     * @param database
     * @param uid
     * @return
     */
    private List<Instance> readInstances(String folder, String database, String uid) {
        List<Instance> instances = null;

        try {

            // Read the event
            System.out.println("Reading instances for event " + uid + " from " + folder + "/" + database + " ...");
            InstancesResponse result = _instanceListApi.folderDatabaseApiCalendarEventsUidInstancesGet(folder,
                                            database, uid);

            instances = result.getInstances();
            if (instances != null) {

                // Dump the events
                System.out.println("Read instance list request succeeded.  Response follows ...\n");
                for (Instance instance : instances) {
                    System.out.println("recurrenceId: " + instance.getRecurrenceId());
                }
            }

            System.out.println();
        }
        catch (ApiException e) {
            System.err.println("Exception when calling InstanceListApi#folderDatabaseApiCalendarEventsUidInstancesGet");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }

        return instances;
    }

    /**
     * Reads a single event instance.
     * 
     * @param folder
     * @param database
     * @param uid
     * @param recurrenceId
     * @return
     */
    private Event readEventInstance(String folder, String database, String uid, String recurrenceId) {
        Event returnEvent = null;

        try {

            // Read the event
            System.out.println("Reading instance " + recurrenceId + " from event " + uid + " from " +
                                        folder + "/" + database + " ...");
            EventResponse result = _eventInstanceApi.folderDatabaseApiCalendarEventsUidRecurrenceIdGet(folder,
                                        database, uid, recurrenceId, null);

            // Does the response have events?
            List<Event> events = result.getEvents();
            if (events != null) {

                // Dump the events
                System.out.println("Read event request succeeded.  Response follows ...\n");
                for (int i = 0; i < events.size(); i++) {
                    Event event = events.get(i);
                    if (i == 0) {
                        returnEvent = event;
                    }

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

                    System.out.println("  recurrenceId: " + event.getRecurrenceId());
                    System.out.println("  sequence: " + event.getSequence());
                    System.out.println("  description: " + event.getDescription());
                }
            }
            else {

                // Unexpected response
                System.out.println("Read event request succeeded, but the response doesn't include a list of events.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling EventInstanceApi#folderDatabaseApiCalendarEventsUidReccurenceIdGet");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }

        return returnEvent;
    }

    /**
     * Deletes a single event instance.
     * 
     * @param folder
     * @param database
     * @param uid
     * @param recurrenceId
     */
    private void deleteEventInstance(String folder, String database, String uid, String recurrenceId) {
        try {

            // Delete the event
            System.out.println("Deleting instance " + recurrenceId + " from event " + uid + " from " + 
                                    folder + "/" + database + " ...");
            _eventInstanceApi.folderDatabaseApiCalendarEventsUidRecurrenceIdDelete(folder, database, uid, 
                                    recurrenceId, null, true);
            System.out.println();
        }
        catch (ApiException e) {
            System.err.println("Exception when calling EventInstanceApi#folderDatabaseApiCalendarEventsUidRecurrenceIdDelete");
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
