package net.fluance.app.spring.core.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import net.fluance.app.spring.core.context.ApplicationContextProvider;


/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

    @Autowired
    protected ApplicationContextProvider applicationContextProvider;

    public Converter<Enum<?>, String> getEnumToStringConverter() {
        return new Converter<Enum<?>, String>() {
        	
            @Override
            public String convert(Enum<?> value) {
                String output = value.toString();
                try {
                    output = applicationContextProvider.getApplicationContext().getMessage(value.toString(),
                                                           null,
                                                           LocaleContextHolder.getLocale());
                } catch (NoSuchMessageException e) {
                    System.err.println("No message resource found for " +
                                       value +
                                       " add this to the resource bundle");
                }
                return output;
            }
        };
    }

     

    public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getEnumToStringConverter());
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }

}
