/**
 * 
 */
package net.fluance.app.spring.core.support.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class StringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

    /* (non-Javadoc)
     * @see org.springframework.core.convert.converter.ConverterFactory#getConverter(java.lang.Class)
     */
    @Override
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> aTargetType) {
        return new StringToEnumConverter<T>(aTargetType);
    }

    @SuppressWarnings("rawtypes")
    private final class StringToEnumConverter<T extends Enum> implements Converter<String, T> {

        private final Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {

            this.enumType = enumType;
        }

        @SuppressWarnings("unchecked")
        public T convert(String source) {

            checkArg(source);
            return (T) Enum.valueOf(enumType, source.trim());
        }

        private void checkArg(String source) {

            // In the spec, null input is not allowed
            if (source == null) {
                throw new IllegalArgumentException("null source is not allowed");
            }
        }
    }
}
