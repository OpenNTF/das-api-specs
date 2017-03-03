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
import io.swagger.client.api.BusytimeApi;
import io.swagger.client.model.BusyTimeResponse;
import io.swagger.client.model.ExDateTimeRange;

import java.util.List;

public class BusyTimeTest {

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
        // - lookupName is the email address of a VALID user.
        //
        // - days is number of days of busy time data to return.
        //
        String basePath = "http://yourserver.yourorg.com";
        String user = "First Last";
        String password = "password";
        String lookupName = "user@yourorg.com";
        Integer days = 7;

        try {
            // Get the API instance and set the base path
            // of the target Domino server
            BusytimeApi busytime = new BusytimeApi();
            ApiClient client = busytime.getApiClient();
            client.setBasePath(basePath);
            client.setUsername(user);
            client.setPassword(password);

            // Request busy time for the user
            System.out.println("Requesting busytime for " + lookupName + " ...");
            BusyTimeResponse result = busytime.apiFreebusyBusytimeGet(lookupName, null, null, null, days);

            // Does the response have busy time data?
            List<ExDateTimeRange> ranges = result.getBusyTimes();
            if (ranges != null) {

                // Dump the busy time blocks
                System.out.println("Busytime request succeeded.  Response follows ...\n");
                for (int i = 0; i < ranges.size(); i++) {
                    ExDateTimeRange range = ranges.get(i);
                    System.out.println("start: " + range.getStart().getDate() + "T" + range.getStart().getTime()
                            + "; end: " + range.getEnd().getDate() + "T" + range.getEnd().getTime());
                }
            }
            else {

                // Unexpected response
                System.out.println("Busytime request succeeded, but the response doesn't include a list of busy times.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling BusytimeApi#apiFreebusyBusytimeGet");
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
