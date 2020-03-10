/**
 * 
 */
package net.fluance.app.web.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.mock.web.DelegatingServletInputStream;

/**
 * @author Yves
 *
 */
public class RequestWrapper  extends HttpServletRequestWrapper {

	private static final Logger LOGGER = LogManager.getLogger(RequestWrapper.class);
	    private final String payload;
	    
	    public RequestWrapper (HttpServletRequest request) throws Exception {
	        super(request);
	        
	        // read the original payload into the payload variable
	        StringBuilder stringBuilder = new StringBuilder();
	        BufferedReader bufferedReader = null;
	        try {
	            // read the payload into the StringBuilder
	            InputStream inputStream = request.getInputStream();
	            if (inputStream != null) {
	                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	                char[] charBuffer = new char[4096];
	                int bytesRead = -1;
	                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                    stringBuilder.append(charBuffer, 0, bytesRead);
	                }
	            } else {
	                // make an empty string since there is no payload
	                stringBuilder.append("");
	            }
	        } catch (IOException ex) {
	        	LOGGER.error("Error reading the request payload", ex);
	            throw new Exception("Error reading the request payload", ex);
	        } finally {
	            if (bufferedReader != null) {
	                try {
	                    bufferedReader.close();
	                } catch (IOException iox) {
	                    // ignore
	                }
	            }
	        }
	        payload = stringBuilder.toString();
	    }
	 
	    @Override
	    public ServletInputStream getInputStream () throws IOException {
	        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payload.getBytes());
	        ServletInputStream inputStream = new DelegatingServletInputStream(byteArrayInputStream);/* {
	        	@Override
	            public int read () 
	                throws IOException {
	                return byteArrayInputStream.read();
	            }
	        };*/
	        return inputStream;
	    }
	
}
