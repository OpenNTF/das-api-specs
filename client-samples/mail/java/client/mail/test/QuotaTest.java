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
import io.swagger.client.api.QuotaApi;
import io.swagger.client.model.QuotaResponse;

public class QuotaTest {

    private QuotaApi _api;

    public QuotaTest(String basePath, String user, String password) {
        _api = new QuotaApi();
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
        // - folder is the mail file database folder. The folder is relative
        //   to the Domino data directory.  Use "." if the mail file
        //   is in the data directory itself.
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
        QuotaTest test = new QuotaTest(basePath, user, password);

        // Read the inbox
        test.readQuota(folder, database);
    }

    /**
     * Reads the quota and dumps it to the console.
     * 
     * @param folder
     * @param database
     */
    private void readQuota(String folder, String database) {
        try {

            // Read the quota
            System.out.println("Reading quota from " + folder + "/" + database + " ...");
            QuotaResponse quota = _api.folderDatabaseApiMailQuotaGet(folder, database); 

            // Does the response have quota information?
            if ( quota != null  ) {
                boolean enabled = false;
                if ( quota.getEnabled() != null ) {
                    enabled = quota.getEnabled();
                }
                
                // Dump the quota
                System.out.println("Read quota request succeeded.  Response follows ...\n");
                System.out.println("  enabled: " + enabled);
                System.out.println("  actualSize: " + quota.getActualSize());
                System.out.println("  usedSize: " + quota.getUsedSize());
                
                if ( enabled ) {
                    System.out.println("  quotaSize: " + quota.getQuotaSize());
                    System.out.println("  warningSize: " + quota.getWarningSize());
                }
                
                System.out.println();
            }
            else {

                // Unexpected response
                System.out.println("Read quota request succeeded, but the response doesn't include quota information.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling QuotaApi#folderDatabaseApiMailQuotaGet");
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
