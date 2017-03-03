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

// IMPORTANT: Change the required package name if necessary.

var api = require('ibm_domino_calendar_api');

// IMPORTANT: Change these values to match your test environment:
//
// - basePath is the base URL for your Domino server.
//   The calendar service MUST be installed and enabled
//   on the server.  Be sure to change the protocol to
//   http if your sever doesn't support https.
//
// - username is the name of a user who has access to your
//   Domino server
//
// - password is the user's password
//
// - folder is the mail file folder name
//
// - database is the mail file database name
//

var basePath = "https://yourserver.yourorg.com";
var username = "First Last";
var password = "password";
var folder = "mail";
var database = "database.nsf"
var accept = false;
var noticeApi = new api.NoticeApi();

/* Callback for accept request */

var acceptCallback = function(error, data, response) {
  if (error) {
    console.error(error);
  }
  else {
    console.log('Notice accepted!');
  }   
}

/* Callback for invitations request */

var invCallback = function(error, data, response) {
  if (error) {
    console.error(error);
  } 
  else if ( null == data ) {
    console.log('Invitations API called succesfully, but the response was empty.');
  }
  else {
    if ( data.hasOwnProperty('notices') ) {
      console.log('Invitations API called successfully. Response follows ...\n');
      for ( var i = 0; i < data.notices.length; i++ ) {
        console.log('summary: ' + data.notices[i].summary);
        console.log('href: ' + data.notices[i].href);
        console.log('');
        
        // Send accept request
        if ( accept ) {
          var href = data.notices[i].href;
          var index = href.lastIndexOf('/');
          if ( index != -1 ) {
            var id = href.substring(index+1);
          
            // Add a comment to the accept action
            var body = new api.ActionRequest();
            body.comments = "I'll be there!";
            var options = {
              'action': body
            };
            
            // Accept it
            console.log('Accepting notice ' + id + ' ...');
                  noticeApi.folderDatabaseApiCalendarNoticesIdActionPut(folder, database, id, "accept", options, acceptCallback);
            console.log('');
          }
        }
      }
    }
    else {
      console.log('Invitations API called successfully, but response does not contain notices.');
    }
  }
};

/* Start of main routine */

// Parse command line arguments

process.argv.forEach(function (val, index, array) {
  if ( index > 1 ) {
    if ( val == "-a" ) {
      accept = true;
    }
  }
});

// Get the API instance and set the base path of 
// the target Domino server

var noticeListApi = new api.NoticeListApi();
noticeListApi.apiClient.basePath = basePath;

// Set user name and password

var basic = noticeListApi.apiClient.authentications['basic'];
basic.username = username;
basic.password = password;

// Send the request

console.log('Requesting invitations for ' + username + ' ...');
noticeListApi.folderDatabaseApiCalendarInvitationsGet(folder, database, null, invCallback);
