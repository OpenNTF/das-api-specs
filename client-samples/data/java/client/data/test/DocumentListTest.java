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
import io.swagger.client.api.DocumentListApi;
import io.swagger.client.model.DocumentListEntry;
import io.swagger.client.model.DocumentListResponse;

public class DocumentListTest {
    
    private DocumentListApi _api = null;
    
    public DocumentListTest(String basePath, String user, String password) {
        
        _api = new DocumentListApi();
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
        // - folder is database folder name relative to the Domino
        //   data directory.  Use "." if the database is in the
        //   data directory itself.
        //
        // - database is the name of the database.  The database
        //   must allow access from the data service.  Use 
        //   "XPagesExt.nsf" if your server has a copy of the
        //   XPages Extension Library demo database.
        //
        // - query is a search string
        //
        String basePath = "http://yourserver.yourorg.com";
        String username = "First Last";
        String password = "password";
        String folder = ".";
        String database = "XPagesExt.nsf";
        String query = "Lorem";
                
        DocumentListTest test = new DocumentListTest(basePath, username, password);
        test.searchDocumentList(folder, database, query);
    }
    
    /**
     * Searches a database for documents matching a query string.
     * 
     * @param folder
     * @param database
     * @param view
     */
    public void searchDocumentList(String folder, String database, String query) {
        
        try {
            // Send search request.
            
            // NOTE: We limit the search results to 10 documents to avoid huge responses.
            // You can change the limit if you want.
            
            System.out.println("Searching database " + database + " for documents matching " + query + " ...");
            DocumentListResponse result = _api.folderDatabaseApiDataDocumentsGet(
                                                folder, database, query, 10, null);
            
            if ( result.size() > 0 ) {
                System.out.println("Request succeeded. The response follows ...\n");

                for ( int i = 0; i < result.size(); i++ ) {
                    DocumentListEntry entry = result.get(i);
                    System.out.println("Entry " + i);
                    
                    String unid = entry.getUnid();
                    System.out.println("   unid: " + unid);
                    
                    String modified = entry.getModified();
                    System.out.println("   modified: " + modified);
                    
                    String href = entry.getHref();
                    System.out.println("   href: " + href);

                    System.out.println();
                }
            }
            else {
                System.out.println("Request succeeded, but the response doesn't include a list of document entries.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling DocumentListApi#folderDatabaseApiDataDocumentsGet");
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
