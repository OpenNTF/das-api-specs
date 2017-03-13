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

var api = require('ibm_domino_data_api');

// IMPORTANT: Change these values to match your test environment:
//
// - basePath is the base URL for your Domino server.
//   The data service MUST be installed and enabled
//   on the server.  Be sure to change the protocol to
//   http if your server doesn't support https.
//
// - username is the name of a user who has access to your
//   Domino server
//
// - password is the user's password
//
// - folder is the database folder name relative to the Domino
//   data directory.  Use '.' if the database is in the data
//   directory itself.
//
// - database is the database file name.  Use 'XPagesExt.nsf'
//   if your server has a copy of the XPages Extension Library
//   demo database.
//
// - opts.form is the name of a form in the database.

var basePath = 'https://yourserver.yourorg.com';
var username = 'First Last';
var password = 'password';
var folder = '.';
var database = 'XPagesExt.nsf';
var opts = { 
  'form': 'AllTypes',
  'computewithform': true
};

/* Runtime globals */

var unid = null;

/* Callback for the document delete request */

var deleteCallback = function(error, data, response) {
  if (error) {
    console.error(error);
  } 
  else {
    console.log('Document delete request succeeded.');
  }
}

/* Callback for the document read request */

var readCallback = function(error, data, response) {
  if (error) {
    console.error(error);
  } 
  else {
    if ( data != null ) {
      // Dump the document
      console.log('Document read request succeeded. Response follows ...\n');
      console.log('   fldText: ' + data['fldText']);
      console.log('   fldNumber: ' + data['fldNumber']);
      console.log('   fldDateTime: ' + data['fldDateTime'].data);

      // Delete the document
      console.log('');
      console.log('Deleting document ' + unid + '...');
      docApi.folderDatabaseApiDataDocumentsUnidDocUnidDelete(folder, database, unid, deleteCallback);
    }
    else {
      console.log('Document read request succeeded, but response is empty.');
    }
  }
};

/* Callback for document create request */

var createCallback = function(error, data, response) {
  if (error) {
    console.error(error);
  } 
  else {
    var location = null;
    if ( response != null && response.header != null && response.header['location'] != null ) {
      location = response.header['location'];
    }
    
    if ( location != null ) {
      console.log('Document create request succeeded. New document URL follows ...');
      console.log('   ' + response.header['location'] + '\n');
      
      // Extract the UNID from the document URL
      var index = location.lastIndexOf('/');
      if ( index != -1 ) {
        unid = location.substring(index+1);
      }
      
      // Send a document read request
      if ( unid != null ) {
        console.log('Reading document ' + unid + '...');
        docApi.folderDatabaseApiDataDocumentsUnidDocUnidGet(folder, database, unid, 
                  {'strongtype': true}, // strongtype=true renders DateTime values as objects 
                  readCallback);
      }
    }
    else {
      console.log('Document create request succeeded, but response does not include a Location header.');
    }
  }
};

/* Start of main routine */

// Get the API instance and set the base path of 
// the target Domino server

var docApi = new api.DocumentApi();
docApi.apiClient.basePath = basePath;

// Set user name and password

var basic = docApi.apiClient.authentications['basic'];
basic.username = username;
basic.password = password;

// Create the document model

var doc = new api.Document();
doc.fldText = "Created by a Node.js client using Swagger generated code";
doc.fldNumber = 999;
doc.fldDateTime = {'data': '2017-09-01T00:00:00Z', 'type': 'datetime'};

// Send the create request

console.log('Creating a new document in database ' + database + ' ...');
docApi.folderDatabaseApiDataDocumentsPost(folder, database, doc, opts, createCallback);
