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

/* Callback for quota request */

var callback = function(error, data, response) {
  if (error) {
    console.error(error);
  } 
  else {
    if ( data != null ) {
      console.log('Quota request succeeded. Response follows ...');
      
      var enabled = false;
      if ( data.enabled != undefined ) {
        enabled = data.enabled;
      }
      
      console.log('  enabled: ' + enabled);
      console.log('  actualSize: ' + data.actualSize);
      console.log('  usedSize: ' + data.usedSize);
      
      if ( enabled ) {
        console.log('  quotaSize: ' + data.quotaSize);
        console.log('  warningSize: ' + data.warningSize);
      }
    }
    else {
      console.log('Send mail request succeeded, but response does not contain a Location header.');
    }
  }
};

/* Start of main routine */

// Get the API instance and set the base path of 
// the target Domino server

var quotaApi = new api.QuotaApi();
quotaApi.apiClient.basePath = basePath;

// Set user name and password

var basic = quotaApi.apiClient.authentications['basic'];
basic.username = username;
basic.password = password;

// Send the request

console.log('Reading quota from ' + database + ' ...');
quotaApi.folderDatabaseApiMailQuotaGet(folder, database, callback);
