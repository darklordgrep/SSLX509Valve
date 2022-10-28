# SSLX509Valve
Common Access Card (CAC) Authentication Tomcat Valve for Oracle Application Express

## Version
~~~
$Revision: 16176 $ $Date: 2020-12-22 13:40:18 -0600 (Tue, 22 Dec 2020) $
~~~

## Notes and Usage
SSLX509Valve is a valve implementation used to get CAC identity information 
to the Application Express Environment. From the Application Express environment,
one can access the identity information through the REMOTE_IDENT and REMOTE_USER
CGI environment variables.

For example, using SQL: 
~~~
select OWA_UTIL.Get_CGI_env('REMOTE_IDENT') client from dual
~~~
returns the following:
~~~ 
CN=SHAKLEFORD.RUSTY.P.5418005251, OU=USA, OU=PKI, OU=DoD, O=U.S. Government, C=US
~~~
Setup: Copy the jar file into which this class was compiled into the lib directory 
on the Application Express ORDS Tomcat server. Edit the conf/server.xml and add 
the following line to the Host element:
~~~
<Valve className="mil.army.usace.freeboard.cac.SSLX509Valve" />
~~~
Also ensure that the Connector element has attribute clientAuth set to true. This valve has
not been tested with clientAuth set to any other value. I suspect it may not work without it.
It would be advisable to ensure that Tomcat CAC authentication setup and confirmed to be working
first before setting this valve to pass the CAC identify information.

After you save conf/server.xml file, restart the Tomcat Server.

