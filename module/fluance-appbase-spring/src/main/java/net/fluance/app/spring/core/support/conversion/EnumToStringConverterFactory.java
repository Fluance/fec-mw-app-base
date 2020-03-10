/**
 * 
 */
package net.fluance.app.spring.core.support.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import net.fluance.app.spring.core.context.ApplicationContextProvider;

public class EnumToStringConverterFactory implements ConverterFactory<Enum<?>, CharSequence> {

    @Autowired
    private ApplicationContextProvider applicationContextProvider;
    
    @Override
    public <T extends CharSequence> Converter<Enum<?>, T> getConverter(Class<T> arg0) {
        return new Converter<Enum<?>, T>() {

            @SuppressWarnings("unchecked")
            @Override
            public T convert(Enum<?> value) {
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
                return (T) output;
            }
        };
    }

}
