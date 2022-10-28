/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 */
package mil.army.usace.freeboard.cac;

//import org.apache.catalina;
import java.io.IOException;
import org.apache.catalina.valves.ValveBase;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import javax.servlet.ServletException;
import java.security.cert.X509Certificate;

/**
 * SSLX509Valve is a valve implementation used to get CAC identity information 
 * to the Application Express Environment. From the Application Express environment,
 * one can access the identity information through the REMOTE_IDENT and REMOTE_USER
 * CGI environment variables.
 * 
 * For example, using SQL: select OWA_UTIL.Get_CGI_env('REMOTE_IDENT') client from dual
 * 
 * returns the following:
 * 
 * CN=SHAKLEFORD.RUSTY.P.5418005251, OU=USA, OU=PKI, OU=DoD, O=U.S. Government, C=US
 * 
 * Setup: Copy the jar file into which this class was compiled into the lib directory 
 * on the Application Express ORDS Tomcat server. Edit the conf/server.xml and add 
 * the following line to the Host element:
 * 
 * <Valve className="mil.army.usace.freeboard.cac.SSLX509Valve" />
 * 
 * Also ensure that the Connector element has attribute clientAuth set to true. This valve has
 * not been tested with clientAuth set to any other value. I suspect it may not work without it.
 * It would be advisable to ensure that Tomcat CAC authentication setup and confirmed to be working
 * first before setting this valve to pass the CAC identify information.
 * 
 * After you save conf/server.xml file, restart the Tomcat Server.
 * 
 * @author grep
 * @version $Revision: 16176 $ $Date: 2020-12-22 13:40:18 -0600 (Tue, 22 Dec 2020) $
 */
public class SSLX509Valve extends ValveBase {
    /**
     * Get the CAC certificate and set the user principal to the Subject DN. This Subject DN is equivalent to
     * the 'SSL_CLIENT_S_DN_CN' Request Header value we were using in Apache.
     * 
     * @param rqst
     * @param rspns
     * @throws IOException
     * @throws ServletException 
     */
    @Override
    public void invoke(Request rqst, Response rspns) throws IOException, ServletException {
       if (rqst.getScheme().equals("https")) {
          X509Certificate[] certs = (X509Certificate[]) rqst.getAttribute("javax.servlet.request.X509Certificate");
          X509Certificate principalCert = certs[0];
          rqst.setUserPrincipal(principalCert.getSubjectDN());
       }
	   if (this.getNext() != null) {
		  this.getNext().invoke(rqst, rspns);
	   }
    }
    
}
