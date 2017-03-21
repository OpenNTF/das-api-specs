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
var basePath = 'http://yourserver.yourorg.com';
var username = 'First Last';
var password = 'password';
var folder = 'mail';
var database = 'database.nsf';

/* Globals */

var view = 'inbox';

/* Callback for message list request */

var callback = function(error, data, response) {
  if (error) {
    console.error(error);
  } 
  else {
    if ( data != null || data.length == 0 ) {
      console.log('Request succeeded. Response follows ...\n');

      for ( var i = 0; i < data.length; i++ ) {
        console.log('Message ' + i);
        console.log('  subject: ' + data[i].subject);
        console.log('  date: ' + data[i].date);
        
        if ( view == "sent" || view == 'drafts' ) {
          console.log('  to: ' + data[i].to.displayName);
        }
        else {
          console.log('  from: ' + data[i].from.displayName);
        }

        console.log();
      }
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
    view = val;
  }
});

// Get the API instance and set the base path of 
// the target Domino server

var api = new api.MessageListApi();
api.apiClient.basePath = basePath;

// Set user name and password

var basic = api.apiClient.authentications['basic'];
basic.username = username;
basic.password = password;

// Set up options

var opts = {
  'count': 5,
  'sortcolumn': 'date',
  'sortorder': 'descending'
};

// Send the request

if ( view == 'sent' ) {
  console.log('Reading sent view from ' + database + ' ...');
  api.folderDatabaseApiMailSentGet(folder, database, opts, callback);
}
else if ( view == 'drafts' ) {
  console.log('Reading drafts view from ' + database + ' ...');
  api.folderDatabaseApiMailDraftsGet(folder, database, opts, callback);
}
else if ( view == 'trash' ) {
  console.log('Reading trash view from ' + database + ' ...');
  api.folderDatabaseApiMailTrashGet(folder, database, opts, callback);
}
else if ( view == 'inbox' ) {
  console.log('Reading inbox from ' + database + ' ...');
  api.folderDatabaseApiMailInboxGet(folder, database, opts, callback);
}
else {
  console.log('Error.  Unknown view: ' + view);
}
