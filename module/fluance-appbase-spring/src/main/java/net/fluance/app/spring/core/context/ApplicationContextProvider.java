/**
 * 
 */
package net.fluance.app.spring.core.context;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    
    private ApplicationContext applicationContext;
    private Logger logger = LogManager.getLogger(ApplicationContextProvider.class);

    /* (non-Javadoc)
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException {
        logger.log(Level.INFO, "setting context");
        this.applicationContext = aApplicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
