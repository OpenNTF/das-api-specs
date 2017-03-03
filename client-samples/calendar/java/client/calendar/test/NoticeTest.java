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
import io.swagger.client.api.NoticeApi;
import io.swagger.client.api.NoticeListApi;
import io.swagger.client.model.ActionRequest;
import io.swagger.client.model.Event;
import io.swagger.client.model.NoticeResponse;
import io.swagger.client.model.NoticeSummariesResponse;
import io.swagger.client.model.NoticeSummary;

import java.util.List;

public class NoticeTest {

    private NoticeListApi _noticeListApi = null;
    private NoticeApi _noticeApi = null;

    public NoticeTest(String basePath, String username, String password) {
        _noticeListApi = new NoticeListApi();
        ApiClient client = _noticeListApi.getApiClient();
        client.setBasePath(basePath);
        client.setUsername(username);
        client.setPassword(password);

        _noticeApi = new NoticeApi();
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
        // - user is the mail file owner's user name (for HTTP authentication).
        //
        // - password is the owner's HTTP password.
        //
        String basePath = "http://yourserver.yourorg.com";
        String folder = "mail";
        String database = "database.nsf";
        String user = "First Last";
        String password = "password";

        // Parse command line
        boolean acceptInvites = false;
        for (int i = 0; i < args.length; i++) {
            if ("-a".equalsIgnoreCase(args[i])) {
                acceptInvites = true;
            }
        }

        // Get an instance of the test class
        NoticeTest test = new NoticeTest(basePath, user, password);

        // Read the list of invitations
        List<NoticeSummary> summaries = test.readInvitations(folder, database);

        // Read individual notices
        if (summaries != null) {
            for (NoticeSummary summary : summaries) {
                String id = null;
                String href = summary.getHref();
                int index = href.lastIndexOf('/');
                if (index != -1) {
                    id = href.substring(index + 1);
                }

                if (id != null) {

                    // Read it
                    test.readNotice(folder, database, id);

                    // Accept the invite
                    if (acceptInvites) {
                        test.acceptInvite(folder, database, id);
                    }
                }
            }
        }

    }

    /**
     * Reads a list of unapplied invitations.
     * 
     * @param folder
     * @param database
     * @return
     */
    private List<NoticeSummary> readInvitations(String folder, String database) {
        List<NoticeSummary> summaries = null;

        try {
            // Request a list of events
            System.out.println("Requesting invitations from " + folder + "/" + database + " ...");
            NoticeSummariesResponse result = _noticeListApi.folderDatabaseApiCalendarInvitationsGet(folder, database, null, null);

            // Extract notice summaries from the response
            if (result != null) {
                summaries = result.getNotices();
            }

            // Does the response have summaries?
            if (summaries != null) {

                // Dump the events
                System.out.println("Get invitations request succeeded.  Response follows ...\n");
                for (int i = 0; i < summaries.size(); i++) {
                    NoticeSummary notice = summaries.get(i);
                    System.out.println("Notice " + i);
                    System.out.println("  summary: " + notice.getSummary());
                    System.out.println("  schedule method: " + notice.getScheduleMethod());
                    System.out.println("  href: " + notice.getHref());

                    System.out.println();
                }
            }
            else {

                // Unexpected response
                System.err.println("Get invitations request succeeded, but the response doesn't include a list of notices.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling NoticeListApi#folderDatabaseApiCalendarInvitationsGet");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }

        return summaries;
    }

    /**
     * Reads a single notice.
     * 
     * @param folder
     * @param database
     * @param id
     */
    private void readNotice(String folder, String database, String id) {
        try {
            // Request a notice
            System.out.println("Requesting notice " + id + " from " + folder + "/" + database + " ...");
            NoticeResponse result = _noticeApi.folderDatabaseApiCalendarNoticesIdGet(folder, database, id, null);

            // Does the response have events?
            List<Event> events = result.getEvents();
            if (events != null) {

                // Dump the events
                System.out.println("Get notice request succeeded.  Response follows ...\n");
                for (int i = 0; i < events.size(); i++) {
                    Event event = events.get(i);
                    System.out.println("Event " + i);
                    System.out.println("  summary: " + event.getSummary());
                    System.out.println("  organizer: " + event.getOrganizer().getDisplayName());

                    if (event.getXLotusNoticetype() != null) {
                        System.out.println("  x-lotus-noticetype: " + event.getXLotusNoticetype().getData());
                    }

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
                System.err.println("Get notice request succeeded, but the response doesn't include a list of events.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling NoticeApi#folderDatabaseApiCalendarNoticesIdGet");
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
     * Accept in invitation notice.
     * 
     * @param folder
     * @param database
     * @param id
     */
    private void acceptInvite(String folder, String database, String id) {
        try {
            ActionRequest body = new ActionRequest();
            body.setComments("I'll be there.");

            // Accept the invitation
            System.out.println("Accepting notice " + id + " from " + folder + "/" + database + " ...");
            ApiResponse<Void> result = _noticeApi.folderDatabaseApiCalendarNoticesIdActionPutWithHttpInfo(folder,
                    database, id, "accept", body);

            // Check the result
            if (result != null && result.getStatusCode() == 200) {
                System.out.println("Invitation was accepted");
            }
            else {
                System.out.println("Unexpected reponse from API");
            }

        }
        catch (ApiException e) {
            System.err.println("Exception when calling NoticeApi#folderDatabaseApiCalendarNoticesIdActionPutWithHttpInfo");
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
