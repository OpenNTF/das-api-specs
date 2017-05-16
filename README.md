<!---
  © Copyright IBM Corp. 2017
  
  Licensed under the Apache License, Version 2.0 (the "License"); 
  you may not use this file except in compliance with the License. 
  You may obtain a copy of the License at:
  
  http://www.apache.org/licenses/LICENSE-2.0 
  
  Unless required by applicable law or agreed to in writing, software 
  distributed under the License is distributed on an "AS IS" BASIS, 
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
  implied. See the License for the specific language governing 
--->

# IBM Domino Access Services API Specifications

Domino Access Services (DAS) is a family of REST APIs.  There are separate 
APIs for:

- Data (since Domino 8.5.3 UP1)
- Calendar (since Domino 9.0.1)
- Freebusy (see [the XPages extension library](http://extlib.openntf.org))
- Mail (see [the XPages extension library](http://extlib.openntf.org))

This repository contains OpenAPI specifications for DAS APIs.
Each API has a separate specification.  For example, the freebusy API 
specification is [freebusy.yaml](freebusy.yaml).

## Uses

You can use these OpenAPI specifications with a broad range of Swagger tools.
For example:

- Use [Swagger Codegen](https://github.com/swagger-api/swagger-codegen) to 
  generate client libraries for most popular languages and frameworks.
  For detailed instructions, see the
  [Generating client code](https://github.com/OpenNTF/das-api-specs/wiki/Generating-client-code)
  article.

- Use [Swagger UI](https://github.com/swagger-api/swagger-ui) to view API 
  documentation.  When you deploy Swagger UI to your own 
  Domino server, you can even try ad-hoc API requests
  without writing a line of client code.  For deployment instructions, see
  the [Swagger UI on Domino](https://github.com/OpenNTF/das-api-specs/wiki/Swagger-UI-on-Domino)
  article.

For a quick demo of the Swagger tools and DAS, see [REST API Demonstration](https://youtu.be/DMatdeFf2HU) on YouTube.
  
## Sample Client Code

This repository also includes DAS client samples.  These samples
demonstrate how to use DAS APIs with Swagger generated client libraries.
(*NOTE:* This repository does not include the generated code.)

The samples are not intended to be full-featured applications.  Rather,
the emphasis is on hands-on learning.  You can quickly customize each
sample for your own Domino server, learn from working code, and gradually
tackle your own application.

To get started with the sample code, see the [DAS client samples](client-samples)
page.

## Specifications by Version

Unless otherwise noted, the OpenAPI specifications in this repository are
for the latest versions of Domino and the extension library. Since you
may be using an earlier version of an API, we intend to archive earlier
versions of the OpenAPI specifications in this repository.

Currently, this repository includes the following versions of each
specification:

| Specification     | Domino Version                                                               | Extension Library Version  |
| ----------------- | ---------------------------------------------------------------------------- | -------------------------- |
| **calendar.yaml** | [901 - 901FP7](specs-by-version/v901/calendar.yaml), [901FP8](calendar.yaml) | [901v00_17](calendar.yaml) |
| **data.yaml**     | [901 - 901FP7](specs-by-version/v901/data.yaml), [901FP8](data.yaml)         | [901v00_17](data.yaml)     |
| **freebusy.yaml** | n/a                                                                          | [901v00_17](freebusy.yaml) |
| **mail.yaml**     | n/a                                                                          | [901v00_17](mail.yaml)     |

## Contributions

We hope you find this repository useful and encourage you contribute
back when possible.  For example:

- If you find a problem with one of the OpenAPI specifications,
  report the issue.  If you are feeling ambitious, fix the
  problem yourself and create a pull request.
  
- If you find a problem with one of the client samples, create
  a pull request and/or report the issue.
  
- Are you an expert in Android, C#, PHP or Ruby? If you don't find
  a client sample for your favorite language or platform,
  write your own sample and create a pull request.

## References

For more information about Domino Access Services, see 
[the DAS topic](https://www-10.lotus.com/ldd/ddwiki.nsf/xpAPIViewer.xsp?lookupName=IBM+Domino+Access+Services+9.0.1#action=openDocument&content=catcontent&ct=api)
on the IBM Notes and Domino Application Development wiki. For DAS APIs
added by the XPages Extension Library, see
[the extlib project on OpenNTF](https://extlib.openntf.org/).

For more about the Swagger framework see [swagger.io](http://swagger.io/)
and the [Swagger repositories](https://github.com/swagger-api) 
on GitHub.

For more about OpenAPI, see the [OpenAPI Specification](https://github.com/OAI/OpenAPI-Specification).

## OpenNTF

This project is an OpenNTF project, and is available under the Apache
Licence V2.0. All other aspects of the project, including contributions,
defect reports, discussions, feature requests and reviews are subject
to the OpenNTF Terms of Use - available at
http://openntf.org/Internal/home.nsf/dx/Terms_of_Use.
