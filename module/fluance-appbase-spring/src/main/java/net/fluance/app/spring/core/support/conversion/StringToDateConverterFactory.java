/**
 * 
 */
package net.fluance.app.spring.core.support.conversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class StringToDateConverterFactory implements ConverterFactory<String, Date> {

    @Override
    public <T extends Date> Converter<String, T> getConverter(Class<T> arg0) {
        return null;
    }

    
    public Converter<String, Date> getStringToDateConverter() {
        return new Converter<String, Date>() {

            @Override
            public Date convert(String source) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    return sdf.parse(source);
                } catch (ParseException e) {
                    return null;
                }
            }
        };
    }
}
