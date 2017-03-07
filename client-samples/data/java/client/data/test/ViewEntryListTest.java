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
import io.swagger.client.api.ViewEntryListApi;
import io.swagger.client.model.ViewEntry;
import io.swagger.client.model.ViewEntryListResponse;

import java.util.List;

public class ViewEntryListTest {
    
    private ViewEntryListApi _api = null;
    
    public ViewEntryListTest(String basePath, String user, String password) {
        
        _api = new ViewEntryListApi();
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
        // - viewName is the name of a view in the database.
        //   The view must allow access from the data service.
        //
        String basePath = "http://yourserver.yourorg.com";
        String username = "First Last";
        String password = "password";
        String folder = ".";
        String database = "XPagesExt.nsf";
        String viewName = "AllTypes";
                
        ViewEntryListTest test = new ViewEntryListTest(basePath, username, password);
        test.readViewEntryList(folder, database, viewName);
    }
    
    /**
     * Reads view entries and writes the response to the console.
     * 
     * @param folder
     * @param database
     * @param view
     */
    public void readViewEntryList(String folder, String database, String view) {
        
        try {
            System.out.println("Requesting a list of view entries ...");
            ViewEntryListResponse result = _api.folderDatabaseApiDataCollectionsNameViewNameGet(
                                                folder, database, view, 
                                                null, null, null, null);
            
            if ( result.size() > 0 ) {
                System.out.println("Request succeeded. An excerpt from the response follows ...\n");

                for ( int i = 0; i < result.size(); i++ ) {
                    if ( i == 5 )
                        break;
                    
                    ViewEntry entry = result.get(i);
                    System.out.println("Entry " + i);
                    
                    String entryId = entry.getEntryid();
                    if ( entryId == null ) {
                        entryId = (String)entry.get("@entryid");
                    }
                    System.out.println("   entryid: " + entryId);
                    
                    String noteId = entry.getNoteid();
                    if ( noteId == null ) {
                        noteId = (String)entry.get("@noteid");
                    }
                    System.out.println("   noteid: " + noteId);
                    
                    String unid = entry.getUnid();
                    if ( unid == null ) {
                        unid = (String)entry.get("@unid");
                    }
                    System.out.println("   unid: " + unid);
                    
                    String fldText = null;
                    if ( entry.get("fldText") instanceof String ) {
                        fldText = (String)entry.get("fldText");
                    }
                    System.out.println("   fldText: " + fldText);

                    Double fldNumber = null;
                    if ( entry.get("fldNumber") instanceof Double ) {
                        fldNumber = (Double)entry.get("fldNumber");
                    }
                    System.out.println("   fldNumber: " + fldNumber);

                    List<?> fldText2 = null;
                    if ( entry.get("fldText2") instanceof List<?> ) {
                        fldText2 = (List<?>)entry.get("fldText2");
                    }
                    System.out.println("   fldText2: " + fldText2);

                    System.out.println();
                }
            }
            else {
                System.out.println("Request succeeded, but the response doesn't include a list of view entries.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling ViewEntryListApi#folderDatabaseApiDataCollectionsNameViewNameGet");
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
