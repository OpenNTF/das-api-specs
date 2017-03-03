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
//   https if necessary.
//
// - username is the name of a user who has access to your
//   Domino server
//
// - password is the user's password
//
// - site is the name of the site to search. Or you can
//   specify the site on the command line.
//
// - frOpts.capacity is number of minimum room capacity.

var basePath = "https://yourserver.yourorg.com";
var username = "First Last";
var password = "password";
var site = "Your Site";
var frOpts = {
  'capacity': 12
};

/* ISO8601 Date formatter */

function ISODateString(d) {
    function pad(n) {return n<10 ? '0'+n : n}
    return d.getUTCFullYear() + '-' + pad(d.getUTCMonth()+1) + '-' +
              pad(d.getUTCDate()) + 'T' + pad(d.getUTCHours()) + ':' +
              pad(d.getUTCMinutes()) + ':' + pad(d.getUTCSeconds()) + 'Z'
}

/* Callback for freerooms request */

var frCallback = function(error, data, response) {
  if (error) {
    console.error(error);
  } 
  else {
    if ( data.hasOwnProperty('rooms') ) {
      console.log('Freerooms API called successfully. Response follows ...\n');

      if ( data.rooms.length > 0 ) {
        for ( var i = 0; i < data.rooms.length; i++ ) {
          console.log('name: ' + data.rooms[i].displayName + '; capacity: ' + data.rooms[i].capacity);
        }
      }
      else {
        console.log('No rooms are available!');
      }
    }
    else {
      console.log('Freerooms API called successfully, but response does not contain rooms data.');
    }
  }
};

/* Start of main routine */

// Parse command line arguments

process.argv.forEach(function (val, index, array) {
  if ( index > 1 ) {
    site = val;
  }
});

// Get the API instance and set the base path of 
// the target Domino server

var freerooms = new api.FreeroomsApi();
freerooms.apiClient.basePath = basePath;

// Set user name and password

var basic = freerooms.apiClient.authentications['basic'];
basic.username = username;
basic.password = password;

// Send the request

var start = new Date(); // Start date is now
var end = new Date(start.getTime() + 30000); // End date is 30 minutes from now
console.log('Requesting free rooms in the next 30 minutes for ' + site + ' ...');
freerooms.apiFreebusyFreeroomsGet(site, ISODateString(start), ISODateString(end), frOpts, frCallback);
