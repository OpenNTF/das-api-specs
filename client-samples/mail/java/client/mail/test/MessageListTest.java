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

package client.mail.test;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.MessageListApi;
import io.swagger.client.model.MessageListEntry;
import io.swagger.client.model.MessageListResponse;
import io.swagger.client.model.Person;

public class MessageListTest {

    private MessageListApi _api;

    public MessageListTest(String basePath, String user, String password) {
        _api = new MessageListApi();
        ApiClient client = _api.getApiClient();
        client.setBasePath(basePath);
        client.setUsername(user);
        client.setPassword(password);
    }

    public static void main(String[] args) {

        // IMPORTANT: Change these values to match your test environment:
        //
        // - basePath is the base URL for your Domino server.
        //   The mail service MUST be installed and enabled
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
        String basePath = "http://yourserver.yourorg.com";
        String folder = "mail";
        String database = "database.nsf";
        String user = "First Last";
        String password = "password";

        // Create an instance of the test class
        MessageListTest test = new MessageListTest(basePath, user, password);

        // Read the inbox
        test.readInbox(folder, database);
    }

    /**
     * Reads the inbox and dumps it's messages to the console.
     * 
     * @param folder
     * @param database
     */
    private void readInbox(String folder, String database) {
        try {

            // Read the inbox
            System.out.println("Reading inbox from " + folder + "/" + database + " ...");
            MessageListResponse result = _api.folderDatabaseApiMailInboxGet(folder, database, 
                                            null,           // start Specifies the starting entry.
                                            5,              // count Specifies the number of entries to return.
                                            null,           // page Page number.
                                            null,           // ps Page size or the number of entries to return.
                                            null,           // search Returns only documents that match a full-text query.
                                            null,           // searchmaxdocs Limits the number of documents returned by a full-text search.
                                            "date",         // sortcolumn Returns entries sorted on a column.
                                            "descending",   // sortorder Specifies the sort order.
                                            null,           // keys Returns entries whose initial characters match keys.
                                            null            // keysexactmatch Returns entries that match keys exactly.
                                            );

            // Does the response have messages?
            if ( result != null && result.size() > 0 ) {

                // Dump the messages
                System.out.println("Read inbox request succeeded.  Response follows ...\n");
                for (int i = 0; i < result.size(); i++) {
                    MessageListEntry message = result.get(i);
                    System.out.println("Message " + i);
                    System.out.println("  subject: " + message.getSubject());
                    System.out.println("  date: " + message.getDate());
                    System.out.println("  href: " + message.getHref());
                    
                    Person person = message.getFrom();
                    if ( person != null ) {
                        System.out.println("  from: " + person.getDisplayName());
                    }
                    
                    System.out.println();
                }
            }
            else {

                // Unexpected response
                System.out.println("Read inbox request succeeded, but the response doesn't include a list of messages.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling MessageListApi#folderDatabaseApiMailInboxGet");
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
