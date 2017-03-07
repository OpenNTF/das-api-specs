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

package client.data.test;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.DatabaseListApi;
import io.swagger.client.model.Database;
import io.swagger.client.model.DatabaseListResponse;

public class DatabaseListTest {
    
    private DatabaseListApi _api = null;
    
    public DatabaseListTest(String basePath, String user, String password) {
        
        _api = new DatabaseListApi();
        ApiClient client = _api.getApiClient();
        client.setBasePath(basePath);
        client.setUsername(user);
        client.setPassword(password);
    }
    
    public static void main(String[] args) {

        // IMPORTANT: Change these values to match your test environment:
        //
        // - basePath is the base URL for your Domino server.
        //   The data service MUST be installed and enabled
        //   on the server. Be sure to change the protocol to
        //   https if necessary.
        //
        // - username is the user name of someone with access
        //   the server.
        //
        // - password is the user's password.
        //
        String basePath = "http://yourserver.yourorg.com";
        String username = "First Last";
        String password = "password";
                
        DatabaseListTest test = new DatabaseListTest(basePath, username, password);
        test.readDatabaseList();
    }
    
    /**
     * Reads the list of databases on a server.
     */
    public void readDatabaseList() {
        
        try {
            System.out.println("Requesting a list of databases ...");
            DatabaseListResponse result = _api.apiDataGet();
            
            if ( result.size() > 0 ) {
                System.out.println("Request succeeded.  An excerpt from the response follows ...\n");

                for ( int i = 0; i < result.size(); i++ ) {
                    if ( i == 5 )
                        break;
                    
                    Database database = result.get(i);
                    System.out.println("Database " + i);
                    System.out.println("   title: " + database.getTitle());
                    System.out.println("   filepath: " + database.getFilepath());
                    System.out.println("   replicaid: " + database.getReplicaid());
                    System.out.println("   template: " + database.getTemplate());
                    System.out.println("   href: " + database.getHref());
                    System.out.println();
                }
            }
            else {
                System.out.println("Request succeeded, but the response doesn't include a list of databases.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling DatabaseListApi#apiDataGet");
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
