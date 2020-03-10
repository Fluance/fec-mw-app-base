/**
 * 
 */
package net.fluance.app.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public abstract class AbstractWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/* (non-Javadoc)
	 * @see org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(ServletContext context) throws ServletException {
		super.onStartup(context);
		onStart(context);
	}
	
	protected abstract void onStart(ServletContext context);

	
	
}
