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
import io.swagger.client.api.EventListApi;
import io.swagger.client.model.Event;
import io.swagger.client.model.EventListResponse;

import java.util.List;

public class EventListTest {

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
        // - user is the user name of the mail file owner (for HTTP authentication)
        //
        // - password is the owner's HTTP password
        //
        String basePath = "http://yourserver.yourorg.com";
        String folder = "mail";
        String database = "database.nsf";
        String user = "First Last";
        String password = "password";

        try {
            // Get the API instance and set the base path
            // of the target Domino server
            EventListApi eventsApi = new EventListApi();
            ApiClient client = eventsApi.getApiClient();
            client.setBasePath(basePath);
            client.setUsername(user);
            client.setPassword(password);

            // Request a list of events
            System.out.println("Requesting events from " + folder + "/" + database + " ...");
            EventListResponse result = eventsApi.folderDatabaseApiCalendarEventsGet(folder, database, 
                                            null,   // format
                                            null,   // since 
                                            null,   // before
                                            null,   // sincenow
                                            null,   // days
                                            null,   // count
                                            null,   // start
                                            null);  // fields

            // Does the response have events?
            List<Event> events = result.getEvents();
            if (events != null) {

                // Dump the events
                System.out.println("Get events request succeeded.  Response follows ...\n");
                for (int i = 0; i < events.size(); i++) {
                    Event event = events.get(i);
                    System.out.println("Event " + i);
                    System.out.println("  summary: " + event.getSummary());
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

                    System.out.println();
                }
            }
            else {

                // Unexpected response
                System.out.println("Get events request succeeded, but the response doesn't include a list of events.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling EventListApi#folderDatabaseApiCalendarEventsGet");
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
