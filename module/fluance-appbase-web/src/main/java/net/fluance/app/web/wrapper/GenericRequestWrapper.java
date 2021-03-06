/**
 * 
 */
package net.fluance.app.web.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Yves
 *
 */
public class GenericRequestWrapper extends HttpServletRequestWrapper {

	private static final int INITIAL_BUFFER_SIZE = 2048;
    private HttpServletRequest origRequest;
    private byte[] reqBytes;
    private boolean firstTime = true;
    private Map<String, String[]> parameterMap = null;
 
    public GenericRequestWrapper(HttpServletRequest arg0) {
        super(arg0);
        origRequest = arg0;
    }
 
    public BufferedReader getReader() throws IOException {
 
        getBytes();
 
        InputStreamReader dave = new InputStreamReader(new ByteArrayInputStream(reqBytes));
        BufferedReader br = new BufferedReader(dave);
        return br;
 
    }
 
//    @Override
//    public ServletInputStream getInputStream() throws IOException {
// 
//        getBytes();
// 
//        ServletInputStream sis = new ServletInputStream() {
//            private int numberOfBytesAlreadyRead;
// 
//            @Override
//            public int read() throws IOException {
//                byte b;
//                if (reqBytes.length > numberOfBytesAlreadyRead) {
//                    b = reqBytes[numberOfBytesAlreadyRead++];
//                } else {
//                    b = -1;
//                }
// 
//                return b;
//            }
// 
//            @Override
//            public int read(byte[] b, int off, int len) throws IOException {
//                if (len > (reqBytes.length - numberOfBytesAlreadyRead)) {
//                    len = reqBytes.length - numberOfBytesAlreadyRead;
//                }
//                if (len <= 0) {
//                    return -1;
//                }
//                System.arraycopy(reqBytes, numberOfBytesAlreadyRead, b, off, len);
//                numberOfBytesAlreadyRead += len;
//                return len;
//            }
// 
//        };
// 
//        return sis;
//    }
 
    public byte[] getBytes() throws IOException {
        if (firstTime) {
            firstTime = false;
            // Read the parameters first, because they can get reachless after the inputStream is read.
            getParameterMap();
            int initialSize = origRequest.getContentLength();
            if (initialSize < INITIAL_BUFFER_SIZE) {
                initialSize = INITIAL_BUFFER_SIZE;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream(initialSize);
            byte[] buf = new byte[1024];
            InputStream is = origRequest.getInputStream();
            int len = 0;
            while (len >= 0) {
                len = is.read(buf);
                if (len > 0) {
                    baos.write(buf, 0, len);
                }
            }
            reqBytes = baos.toByteArray();
        }
        return reqBytes;
    }
 
    @Override
    public String getParameter(String name) {
        String[] a = parameterMap.get(name);
        if (a == null || a.length == 0) {
            return null;
        }
        return a[0];
    }
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Map getParameterMap() {
        if (parameterMap == null) {
            parameterMap = new HashMap<String, String[]>();
            parameterMap.putAll(super.getParameterMap());
        }
        return parameterMap;
    }
 
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Enumeration getParameterNames() {
        return Collections.enumeration(parameterMap.values());
    }
 
    @Override
    public String[] getParameterValues(String name) {
        return parameterMap.get(name);
    }
	
}
