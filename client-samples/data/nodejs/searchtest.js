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

var basePath = 'https://yourserver.yourorg.com';
var username = 'First Last';
var password = 'password';
var folder = '.';
var database = 'XPagesExt.nsf';

/* Globals */

var searchText = "Lorem";

/* Callback for document search request */

var callback = function(error, data, response) {
  if (error) {
    console.error(error);
  } 
  else {
    debugger;
    if ( data.length > 0 ) {
      console.log('Document search API called successfully. Response follows ...\n');
      for ( var i = 0; i < data.length; i++ ) {
        console.log('Document ' + i);
        console.log('   unid: ' + data[i]['@unid']);
        console.log('   modified: ' + data[i]['@modified']);
        console.log('   href: ' + data[i]['@href']);
        console.log('');
      }
    }
    else {
      console.log('Document search API called successfully, but response does not contain a list of documents.');
    }
  }
};

/* Start of main routine */

// Parse command line arguments

process.argv.forEach(function (val, index, array) {
  if ( index > 1 ) {
    searchText = val;
  }
});

// Get the API instance and set the base path of 
// the target Domino server

var docListApi = new api.DocumentListApi();
docListApi.apiClient.basePath = basePath;

// Set user name and password

var basic = docListApi.apiClient.authentications['basic'];
basic.username = username;
basic.password = password;

// Set search string and maximum # of docs to return.
// We limit the number of documents to 10 to avoid huge
// responses, but you can change that.

var opts = { 
  'search': searchText,
  'searchmaxdocs': 10
};

// Send the request

console.log('Searching documents in database ' + database + ' matching ' + searchText + ' ...');
docListApi.folderDatabaseApiDataDocumentsGet(folder, database, opts, callback);
