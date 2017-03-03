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

package client.freebusy.test;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SitesApi;
import io.swagger.client.model.DirectoriesResponse;
import io.swagger.client.model.Directory;
import io.swagger.client.model.Site;
import io.swagger.client.model.SitesResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class SitesTest {

    public static void main(String[] args) throws UnsupportedEncodingException {

        // IMPORTANT: Change these values to match your test environment:
        //
        // - basePath is the base URL for your Domino server.
        //   The freebusy service MUST be installed and enabled
        //   on the server. Be sure to change the protocol to
        //   https if necessary.
        //
        // - user is the user name for HTTP authentication
        //
        // - password is the user's HTTP password
        //
        String basePath = "http://yourserver.yourorg.com";
        String user = "First Last";
        String password = "password";

        // Get the API instance and set the base path
        // of the target Domino server
        SitesApi sitesApi = new SitesApi();
        ApiClient client = sitesApi.getApiClient();
        client.setBasePath(basePath);
        client.setUsername(user);
        client.setPassword(password);

        DirectoriesResponse result = null;
        try {
            // Request directories
            System.out.println("Requesting directories ...");
            result = sitesApi.apiFreebusyDirectoriesGet();
        }
        catch (ApiException e) {
            System.err.println("Exception when calling SitesApi#apiFreebusyDirectoriesGet");
            String body = e.getResponseBody();
            if (body != null) {
                System.err.println("Response from server ...");
                System.err.println(body);
            }
            else {
                e.printStackTrace();
            }
        }

        String href = null;
        if (result != null) {
            // Does the response have directories?
            List<Directory> directories = result.getDirectories();
            if (directories != null) {

                // Dump the directories
                System.out.println("Directories request succeeded.  Response follows ...\n");
                for (int i = 0; i < directories.size(); i++) {
                    Directory dir = directories.get(i);
                    System.out.println("name: " + dir.getDisplayName());

                    if (i == 0) {
                        href = dir.getLinks().get(0).getHref();
                    }
                }
            }
            else {

                // Unexpected response
                System.out.println("Directories request succeeded, but the response doesn't include a list of directories.");
            }
        }

        // Extract directory ID from href
        String directoryId = null;
        if (href != null) {
            String encoded = href.substring(href.lastIndexOf("/") + 1, href.length());
            directoryId = URLDecoder.decode(encoded, "UTF-8");
        }

        if (directoryId != null) {

            SitesResponse sitesResult = null;
            try {
                // Request directories
                System.out.println("\nRequesting sites for " + directoryId + " ...");
                sitesResult = sitesApi.apiFreebusySitesDirectoryGet(directoryId);
            }
            catch (ApiException e) {
                System.err.println("Exception when calling SitesApi#apiFreebusySitesDirectoryGet");
                String body = e.getResponseBody();
                if (body != null) {
                    System.err.println("Response from server ...");
                    System.err.println(body);
                }
                else {
                    e.printStackTrace();
                }
            }

            if (sitesResult != null) {

                // Does the response have sites?
                List<Site> sites = sitesResult.getSites();
                if (sites != null) {

                    // Dump the sites
                    System.out.println("Sites request succeeded.  Response follows ...\n");
                    for (int i = 0; i < sites.size(); i++) {
                        Site site = sites.get(i);
                        System.out.println("name: " + site.getDisplayName());
                    }
                }
                else {

                    // Unexpected response
                    System.out.println("Sites request succeeded, but the response doesn't include a list of sites.");
                }
            }
        }
    }

}
