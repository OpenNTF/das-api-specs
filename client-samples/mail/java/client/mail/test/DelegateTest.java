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
import io.swagger.client.api.DelegateApi;
import io.swagger.client.model.Access;
import io.swagger.client.model.Delegate;

public class DelegateTest {
    
    private static final String DELEGATE_NAME = "Test Delegate/Your Org";

    private DelegateApi _api;

    public DelegateTest(String basePath, String user, String password) {
        _api = new DelegateApi();
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
        
        DelegateTest test = new DelegateTest(basePath, user, password);
        
        // Create a delegate with create access (but not edit access)
        
        Access access = new Access().what("mail").create(true);
        Delegate delegate = new Delegate().name(DELEGATE_NAME).access(access);
        boolean created = test.createDelegate(folder, database, delegate);

        // Stop now if we didn't create the delegate 
        
        if ( !created ) {
            return;
        }
        
        // Read and verify the delegate
        
        delegate = test.readDelegate(folder, database, DELEGATE_NAME);
        if ( delegate != null ) {
            boolean valid = true;

            if ( ! "mail".equals(delegate.getAccess().getWhat()) ) {
                System.err.println("Unexpected access type: " + delegate.getAccess().getWhat());
                valid = false;
            }

            if ( delegate.getAccess().getEdit() ) {
                System.err.println("Unexpected edit access. Should be false.");
                valid = false;
            }

            if ( ! delegate.getAccess().getCreate() ) {
                System.err.println("Unexpected create access. Should be true.");
                valid = false;
            }

            if ( valid ) {
                System.out.println("All delegate object values are as expected.");
            }
            System.out.println();
        }
        
        // Delete the delegate
        
        test.deleteDelegate(folder, database, DELEGATE_NAME);
    }

    /**
     * Creates a delegate.
     * 
     * @param folder
     * @param database
     */
    private boolean createDelegate(String folder, String database, Delegate delegate) {
        boolean created = false;
        
        try {

            // Create the delegate
            System.out.println("Creating delegate " + delegate.getName() + " in " + folder + "/" + database + " ...");
            _api.folderDatabaseApiMailDelegatesPost(folder, database, delegate); 

            System.out.println("Create delegate request succeeded.\n");
            created = true;
        }
        catch (ApiException e) {
            System.err.println("Exception when calling DelegateApi#folderDatabaseApiMailDelegatesPost");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }
        
        return created;
    }

    /**
     * Reads a delegate.
     * 
     * @param folder
     * @param database
     */
    private Delegate readDelegate(String folder, String database, String name) {
        Delegate delegate = null;
        
        try {

            // Read the delegate
            System.out.println("Reading delegate " + name + " from " + folder + "/" + database + " ...");
            delegate = _api.folderDatabaseApiMailDelegatesNameGet(folder, database, name); 

            // Does the response have a delegate?
            if ( delegate == null  ) {

                // Unexpected response
                System.out.println("Read delegate request succeeded, but the response doesn't include delegate information.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling DelegateApi#folderDatabaseApiMailDelegatesNameGet");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }
        
        return delegate;
    }

    /**
     * Deletes a delegate.
     * 
     * @param folder
     * @param database
     */
    private void deleteDelegate(String folder, String database, String name) {
        
        try {

            // Delete the delegate
            System.out.println("Deleting delegate " + name + " from " + folder + "/" + database + " ...");
            _api.folderDatabaseApiMailDelegatesNameDelete(folder, database, name); 

        }
        catch (ApiException e) {
            System.err.println("Exception when calling DelegateApi#folderDatabaseApiMailDelegatesNameDelete");
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
