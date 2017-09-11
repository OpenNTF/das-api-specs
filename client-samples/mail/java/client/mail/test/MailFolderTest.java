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
import io.swagger.client.ApiResponse;
import io.swagger.client.api.FolderApi;
import io.swagger.client.api.FolderListApi;
import io.swagger.client.model.FolderListResponse;
import io.swagger.client.model.MailFolder;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MailFolderTest {

    private FolderListApi _folderListApi;
    private FolderApi _folderApi;

    public MailFolderTest(String basePath, String user, String password) {
        _folderListApi = new FolderListApi();
        ApiClient client = _folderListApi.getApiClient();
        client.setBasePath(basePath);
        client.setUsername(user);
        client.setPassword(password);
        
        _folderApi = new FolderApi();
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
        String testFolderName = "Folder API Test";

        // Create an instance of the test class

        MailFolderTest test = new MailFolderTest(basePath, user, password);

        // Read the list of folders
        
        List<MailFolder> folders = test.readFolderList(folder, database);
        if ( folders == null ) {
            System.err.println("Cannot read existing folders. Stopping tests.");
            return;
        }
        
        // Make sure test folder doesn't already exist
        
        for ( MailFolder mailFolder : folders ) {
            if ( testFolderName.equalsIgnoreCase(mailFolder.getName())) {
                System.err.println("Test folder already exists: " + testFolderName);
                System.err.println("Stopping tests.  Resolve the conflict and try again.");
                return;
            }
        }
        
        // Create a new folder
        
        String folderUnid = test.createFolder(folder, database, testFolderName);
        if ( folderUnid != null ) {
        
            // TODO: Add tests for adding and removing messages
            
            // Delete the folder
            
            test.deleteFolder(folder, database, folderUnid);
        }
    }

    /**
     * Reads a list of mail folders.
     * 
     * @param folder
     * @param database
     */
    private List<MailFolder> readFolderList(String folder, String database) {
        List<MailFolder> folders = null;
        
        try {

            // Read the folder list
            System.out.println("Reading folder list from " + folder + "/" + database + " ...");
            FolderListResponse response = _folderListApi.folderDatabaseApiMailFoldersGet(folder, database); 

            // Does the response have a folder list?
            if ( response != null  ) {
                folders = response;
                
                // Dump the quota
                System.out.println("Read folder list request succeeded.  Number of folders: " + folders.size() + "\n");
            }
            else {

                // Unexpected response
                System.out.println("Read folder list request succeeded, but the response doesn't include a list of folders.");
            }
        }
        catch (ApiException e) {
            System.err.println("Exception when calling FolderListApi#folderDatabaseApiMailFoldersGet");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }
        
        return folders;
    }

    /**
     * Creates a mail folder.
     * 
     * @param folder
     * @param database
     * @param mailFolderName
     * @return
     */
    private String createFolder(String folder, String database, String mailFolderName) {
        String unid = null;
        
        try {
            MailFolder mailFolder = new MailFolder();
            mailFolder.setName(mailFolderName);
            
            // Read the folder list
            System.out.println("Creating folder " + mailFolderName + " ...");
            ApiResponse<Void> response = _folderApi.folderDatabaseApiMailFoldersPostWithHttpInfo(folder, database, mailFolder); 

            // Extract the Location header
            String location = null;
            Map<String, List<String>> headers = response.getHeaders();
            if (headers != null) {
                Set<String> keys = headers.keySet();
                for (String key : keys) {
                    if (key.equalsIgnoreCase("Location")) {
                        location = headers.get(key).get(0);
                        break;
                    }
                }
            }

            // Extract the UID from the location
            if (location != null) {
                // Dump the Location header
                System.out.println("Create folder request succeeded.  Location is " + location);

                int index = location.lastIndexOf('/');
                if (index != -1) {
                    unid = location.substring(index + 1, location.length());
                }
            }
            else {
                // Unexpected response
                System.out.println("Create folder request succeeded, but the response doesn't include a Location header.");
            }

            System.out.println();
        }
        catch (ApiException e) {
            System.err.println("Exception when calling FolderApi#folderDatabaseApiMailFoldersPostWithHttpInfo");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }
        
        return unid;
    }

    /**
     * Deletes a mail folder.
     * 
     * @param folder
     * @param database
     * @param unid
     */
    private void deleteFolder(String folder, String database, String unid) {
        
        try {

            // Delete the folder
            System.out.println("Deleting folder " + unid + " from " + folder + "/" + database + " ...");
            _folderApi.folderDatabaseApiMailFoldersUnidDelete(folder, database, unid);
            
        }
        catch (ApiException e) {
            System.err.println("Exception when calling FolderApi#folderDatabaseApiMailFoldersUnidDelete");
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
