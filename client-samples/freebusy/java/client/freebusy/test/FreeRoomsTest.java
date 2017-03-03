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

package client.freebusy.test;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.FreeroomsApi;
import io.swagger.client.model.FreeRoomsResponse;
import io.swagger.client.model.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class FreeRoomsTest {

    public static void main(String[] args) {

        // IMPORTANT: Change these values to match your test environment:
        //
        // - basePath is the base URL for your Domino server.
        //   The freebusy service MUST be installed and enabled
        //   on the server. Be sure to change the protocol to
        //   https if necessary.
        //
        // - user is the user name for HTTP authentication
        //
        // - password is the user's HTTP password
        //
        // - site is the name of a site to search.
        //
        // - capacity is the minimum room capacity.
        //
        String basePath = "http://yourserver.yourorg.com";
        String user = "First Last";
        String password = "password";
        String site = "Your Site";
        Integer capacity = 12;

        // Get an ISO8601 date formatter
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        formatter.setTimeZone(tz);

        // We'll request rooms available for the next 30 minutes
        Date start = new Date();
        Date end = new Date(start.getTime() + 30000);

        try {
            // Get the API instance and set the base path
            // of the target Domino server
            FreeroomsApi freerooms = new FreeroomsApi();
            ApiClient client = freerooms.getApiClient();
            client.setBasePath(basePath);
            client.setUsername(user);
            client.setPassword(password);

            // Request available rooms
            System.out.println("Requesting free rooms in the next 30 minutes for " + site + " ...");
            FreeRoomsResponse result = freerooms.apiFreebusyFreeroomsGet(site, formatter.format(start),
                                            formatter.format(end), capacity);

            // Does the response have rooms
            List<Room> rooms = result.getRooms();
            if (rooms != null) {

                // Dump the list of available rooms
                System.out.println("Freerooms request succeeded.  Response follows ...\n");
                for (int i = 0; i < rooms.size(); i++) {
                    Room room = rooms.get(i);
                    System.out.println("name: " + room.getDisplayName() + "; capacity: " + room.getCapacity());
                }
            }
            else {

                // No rooms available or unexpected response
                System.out.println("Freerooms request succeeded, but the response doesn't include a list of rooms.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling FreeroomsApi#apiFreebusyFreeroomsGet");
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
