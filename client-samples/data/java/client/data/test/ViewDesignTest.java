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
import io.swagger.client.api.ViewDesignApi;
import io.swagger.client.model.ViewColumnDesign;
import io.swagger.client.model.ViewDesignResponse;

public class ViewDesignTest {
    
    private ViewDesignApi _api = null;
    
    public ViewDesignTest(String basePath, String user, String password) {
        
        _api = new ViewDesignApi();
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
                
        ViewDesignTest test = new ViewDesignTest(basePath, username, password);
        test.readViewDesign(folder, database, viewName);
    }
    
    /**
     * Reads a view design and writes the response to the console.
     * 
     * @param folder
     * @param database
     * @param view
     */
    public void readViewDesign(String folder, String database, String view) {
        
        try {
            System.out.println("Requesting the design of view " + view + "...");
            ViewDesignResponse result = _api.folderDatabaseApiDataCollectionsNameViewNameDesignGet(
                                                folder, database, view);
            
            if ( result.size() > 0 ) {
                System.out.println("Request succeeded. An excerpt from the response follows ...\n");

                for ( int i = 0; i < result.size(); i++ ) {
                    if ( i == 5 )
                        break;
                    
                    ViewColumnDesign column = result.get(i);
                    System.out.println("Column " + i);
                    
                    String name = column.getName();
                    System.out.println("   name: " + name);
                    
                    String title = column.getTitle();
                    System.out.println("   title: " + title);
                    
                    Integer width = column.getWidth();
                    System.out.println("   width: " + width);

                    Boolean hidden = column.getHidden();
                    System.out.println("   hidden: " + hidden);

                    System.out.println();
                }
            }
            else {
                System.out.println("Request succeeded, but the response doesn't include a list of view entries.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling ViewDesignApi#folderDatabaseApiDataCollectionsNameViewNameDesignGet");
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
