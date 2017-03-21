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

var api = require('ibm_domino_mail_api');

// IMPORTANT: Change these values to match your test environment:
//
// - basePath is the base URL for your Domino server.
//   The mail service MUST be installed and enabled
//   on the server.  Be sure to change the protocol to
//   http if your server doesn't support https.
//
// - username is the name of a user who has access to a
//   mail file on your Domino server
//
// - password is the user's password
//
// - folder is the mail file folder name relative to the Domino
//   data directory.  Use '.' if the mail file is in the data
//   directory itself.
//
// - database is the mail file name.
//
// - sendTo is the email address of a recipient.  Or you can
//   specify the recipient on the command line.  This test
//   sends a message to one recipient.
//

var basePath = 'https://yourserver.yourorg.com';
var username = 'First Last';
var password = 'password';
var folder = 'mail';
var database = 'database.nsf';
var sendTo = 'recipient@yourorg.com';

/* Callback for send request */

var callback = function(error, data, response) {
  if (error) {
    console.error(error);
  } 
  else {
    var location = null;
    if ( response != null && response.header != null && response.header['location'] != null ) {
      location = response.header['location'];
    }
    
    if ( location != null ) {
      console.log('Send mail request succeeded. New document URL follows ...');
      console.log('   ' + response.header['location'] + '\n');
    }
    else {
      console.log('Send mail request succeeded, but response does not contain a Location header.');
    }
  }
};

/* Start of main routine */

// Parse command line arguments

process.argv.forEach(function (val, index, array) {
  if ( index > 1 ) {
    sendTo = val;
  }
});

// Get the API instance and set the base path of 
// the target Domino server

var messageApi = new api.MessageApi();
messageApi.apiClient.basePath = basePath;

// Set user name and password

var basic = messageApi.apiClient.authentications['basic'];
basic.username = username;
basic.password = password;

// Create a local representation of the message to send

var message = new api.Message();
message.subject = "Test message from Swagger generated Javascript client code";
message.to = [ { 'email': sendTo } ];
message.content = [ 
  {
    'contentType': 'multipart/alternative; Boundary=abcdefg'
  },
  {
    'contentType': 'text/plain',
    'boundary': '--abcdefg',
    'data': 'This is a test.'
  },
  {
    'contentType': 'text/html',
    'boundary': '--abcdefg',
    'data': '<html><body>This is a test.<p><b>This is only a test!</b></body></html>'
  } 
];

// Send the request

console.log('Sending message to ' + sendTo + ' ...');
messageApi.folderDatabaseApiMailOutboxPost(folder, database, message, callback);
