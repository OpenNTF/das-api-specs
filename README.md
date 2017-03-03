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
Each API has a separate specification.  For example, for the freebusy API 
specification, see [freebusy.yaml](freebusy.yaml).

## Uses

You can use these API specifications with a broad range of Swagger tools.
For example:

- Use [Swagger Codegen](https://github.com/swagger-api/swagger-codegen) to 
  generate client libraries for most popular languages and frameworks.

- Use [Swagger UI](https://github.com/swagger-api/swagger-ui) to view API 
  documentation.  When you deploy Swagger UI to your own 
  Domino server, you can even try ad-hoc API requests
  without writing a line of client code.
  
## Sample Client Code

To be completed ...

## Contributions

To be completed ...

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
