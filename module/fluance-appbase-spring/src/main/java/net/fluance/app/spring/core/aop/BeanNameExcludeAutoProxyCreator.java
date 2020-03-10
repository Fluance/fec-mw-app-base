/**
 * 
 */
package net.fluance.app.spring.core.aop;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
public class BeanNameExcludeAutoProxyCreator extends BeanNameAutoProxyCreator {

    private String[] beanNamesToExclude;

    public BeanNameExcludeAutoProxyCreator() {
    }

    /**
     * Noms des beans � exclure.
     */
    public void setExcludeBeanNames(String[] aBeanNamesToExclude) {
        if(aBeanNamesToExclude == null) {
        	throw new IllegalArgumentException("bean names list must not be null");
        }
        if(aBeanNamesToExclude.length == 0) {
        	throw new IllegalArgumentException("bean names list must not be empty");
        }
        this.beanNamesToExclude = new String[aBeanNamesToExclude.length];
        for (int i = 0; i < aBeanNamesToExclude.length; i++) {
            this.beanNamesToExclude[i] = StringUtils
                .trimWhitespace(aBeanNamesToExclude[i]);
        }
    }

    @Override
    protected boolean isMatch(String aBeanName, String aMappedName) {
        if (super.isMatch(aBeanName, aMappedName)) {
            return !PatternMatchUtils.simpleMatch(beanNamesToExclude, aBeanName);
        } else {
            return false;
        }
    }

	public String[] getBeanNamesToExclude() {
		return beanNamesToExclude;
	}

	public void setBeanNamesToExclude(String[] beanNamesToExclude) {
		this.beanNamesToExclude = beanNamesToExclude;
	}
    
}
