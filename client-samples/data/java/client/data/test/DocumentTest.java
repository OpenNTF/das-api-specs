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
import io.swagger.client.ApiResponse;
import io.swagger.client.api.DocumentApi;
import io.swagger.client.model.Document;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DocumentTest {
    
    static private final String FIELD_TEXT = "fldText";
    static private final String FIELD_NUMBER = "fldNumber";
    static private final String FIELD_DATETIME = "fldDateTime";
    
    static private final String PROP_DATA = "data";
    static private final String PROP_TYPE = "type";
    static private final String PROP_TYPE_DATETIME = "datetime";
    
    private DocumentApi _api = null;
    
    public DocumentTest(String basePath, String user, String password) {
        
        _api = new DocumentApi();
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
        // - formName is the name of a form in the database.
        //
        String basePath = "http://yourserver.yourorg.com";
        String username = "First Last";
        String password = "password";
        String folder = ".";
        String database = "XPagesExt.nsf";
        String formName = "AllTypes";

        // Get an instance of the test class
        
        DocumentTest test = new DocumentTest(basePath, username, password);
        
        // Create a local model of the document.  The document has:
        // - One text field
        // - One number field
        // - One DateTime field
        
        Document document = new Document();
        document.put(FIELD_TEXT, "This is a test from a Java client");
        document.put(FIELD_NUMBER, new Double(999));
        
        HashMap<String, Object> fldDateTime = new HashMap<String, Object>();
        fldDateTime.put(PROP_DATA, "2011-02-23T13:09:23Z");
        fldDateTime.put(PROP_TYPE, PROP_TYPE_DATETIME);
        document.put(FIELD_DATETIME, fldDateTime);
        
        // Send the document to the data API
        
        String unid = test.createDocument(folder, database, document, formName);
        
        // Verify results and delete the document
        
        if ( unid != null ) {
            
            // Read the document
            
            document = test.readDocument(folder, database, unid);

            // Verify the results

            if ( !(document.get(FIELD_TEXT) instanceof String) ) {
                System.err.println(FIELD_TEXT + " property is missing or is not a string.");
            }
            
            if ( !(document.get(FIELD_NUMBER) instanceof Double) ) {
                System.err.println(FIELD_NUMBER + " property is missing or is not a number.");
            }
            
            String dataType = null;
            if ( document.get(FIELD_DATETIME) instanceof AbstractMap<?, ?> ) {
                AbstractMap<String, Object> dateTimeField = null;
                dateTimeField = (AbstractMap<String, Object>)document.get(FIELD_DATETIME);
                dataType = (String)dateTimeField.get(PROP_TYPE);
            }
            
            if ( !PROP_TYPE_DATETIME.equals(dataType) ) {
                System.err.println(FIELD_DATETIME + " property is missing or has unexpected format");
            }
            
            // Remove the number field
            
            document.remove(FIELD_NUMBER);
            
            // Update the document
            
            test.updateDocument(folder, database, unid, document, formName);

            // Read it again to verify the field has been removed

            document = test.readDocument(folder, database, unid);
            if ( document.get(FIELD_NUMBER) != null ) {
                System.err.println(FIELD_NUMBER + " exists. It should have been removed by the update.");
            }
            
            // Delete the document
            
            test.deleteDocument(folder, database, unid);
        }
    }
    
    /**
     * Creates a new document.
     * 
     * @param folder
     * @param database
     * @param document
     * @param form
     * @return
     */
    public String createDocument(String folder, String database, Document document, String form) {
        String unid = null;
        
        try {
            System.out.println("Creating a new document ...");
            ApiResponse<Void> result = _api.folderDatabaseApiDataDocumentsPostWithHttpInfo(
                                                folder, database, document, 
                                                form, true, null); 
            
            // Extract the Location header
            String location = null;
            Map<String, List<String>> headers = result.getHeaders();
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
                System.out.println("Create document request succeeded.  Location is " + location);

                int index = location.lastIndexOf('/');
                if (index != -1) {
                    unid = location.substring(index + 1, location.length());
                }
            }
            else {
                // Unexpected response
                System.out.println("Create document request succeeded, but the response doesn't include a Location header.");
            }

            System.out.println();
        }
        catch (ApiException e) {
            System.err.println("Exception when calling DocumentApi#folderDatabaseApiDataDocumentsPostWithHttpInfo");
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
     * Reads a document.
     * 
     * @param folder
     * @param database
     * @param unid
     */
    public Document readDocument(String folder, String database, String unid) {
        Document document = null;
        
        try {
            System.out.println("Reading document " + unid + "...");
            document = _api.folderDatabaseApiDataDocumentsUnidDocUnidGet(folder, database, unid,
                                false, false, true, null);
            
            if ( document != null ) {
                System.out.println("Read document request succeeded");
            }
            else {
                System.out.println("Read document request succeeded, but did not return a document as expected.");
            }
            
            System.out.println();
        }
        catch (ApiException e) {
            System.err.println("Exception when calling DocumentApi#folderDatabaseApiDataDocumentsUnidDocUnidGet");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }
        
        return document;
    }
    
    /**
     * Updates a document.
     * 
     * @param folder
     * @param database
     * @param unid
     */
    public Document updateDocument(String folder, String database, String unid, Document document, String form) {
        
        try {
            System.out.println("Updating document " + unid + "...");
            _api.folderDatabaseApiDataDocumentsUnidDocUnidPut(folder, database, unid,
                                document, form, false, null);
            
            System.out.println("Update document request succeeded\n");
        }
        catch (ApiException e) {
            System.err.println("Exception when calling DocumentApi#folderDatabaseApiDataDocumentsUnidDocUnidPut");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }
        
        return document;
    }
    
    /**
     * Deletes a document.
     * 
     * @param folder
     * @param database
     * @param unid
     */
    public void deleteDocument(String folder, String database, String unid) {
        try {
            System.out.println("Deleting document " + unid + "...");
            _api.folderDatabaseApiDataDocumentsUnidDocUnidDelete(folder, database, unid, null);
            
        }
        catch (ApiException e) {
            System.err.println("Exception when calling DocumentApi#folderDatabaseApiDataDocumentsUnidDocUnidDelete");
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
