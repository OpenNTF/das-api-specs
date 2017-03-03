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

var api = require('ibm_domino_freebusy_api');

// IMPORTANT: Change these values to match your test environment:
//
// - basePath is the base URL for your Domino server.
//   The freebusy service MUST be installed and enabled
//   on the server.  Be sure to change the protocol to
//   http if your sever doesn't support https.
//
// - username is the name of a user who has access to your
//   Domino server
//
// - password is the user's password
//
// - lookupName is the email address of a VALID user. Or you
//   can specify the lookup name on the command line.
//
// - btOpts.days is number of days of busy time data to return.

var basePath = "https://yourserver.yourorg.com";
var username = "First Last";
var password = "password";
var lookupName = "user@yourorg.com";
var btOpts = { 
  'days': 7
};

/* Callback for busytime request */

var btCallback = function(error, data, response) {
  if (error) {
    console.error(error);
  } 
  else {
    if ( data.hasOwnProperty('busyTimes') ) {
      console.log('Busytime API called successfully. Response follows ...\n');
      for ( var i = 0; i < data.busyTimes.length; i++ ) {
        console.log('start: ' + data.busyTimes[i].start.date + 'T' + data.busyTimes[i].start.time +
          '; end: ' + data.busyTimes[i].end.date + 'T' + data.busyTimes[i].end.time
        );
      }
    }
    else {
      console.log('Busytime API called successfully, but response does not contain busytime data.');
    }
  }
};

/* Start of main routine */

// Parse command line arguments

process.argv.forEach(function (val, index, array) {
  if ( index > 1 ) {
    lookupName = val;
  }
});

// Get the API instance and set the base path of 
// the target Domino server

var busytime = new api.BusytimeApi();
busytime.apiClient.basePath = basePath;

// Set user name and password

var basic = busytime.apiClient.authentications['basic'];
basic.username = username;
basic.password = password;

// Send the request

console.log('Requesting busytime for ' + lookupName + ' ...');
busytime.apiFreebusyBusytimeGet(lookupName, btOpts, btCallback);
