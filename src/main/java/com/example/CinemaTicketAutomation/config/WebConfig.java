package com.example.CinemaTicketAutomation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CaseInsensitiveEnumConverterFactory());
    }

    @Component
    public static class CaseInsensitiveEnumConverterFactory implements ConverterFactory<String, Enum> {

        @Override
        public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
            return new CaseInsensitiveEnumConverter<>(targetType);
        }

        private static class CaseInsensitiveEnumConverter<T extends Enum> implements Converter<String, T> {

            private final Class<T> enumType;

            public CaseInsensitiveEnumConverter(Class<T> enumType) {
                this.enumType = enumType;
            }

            @Override
            public T convert(String source) {
                if (source.isEmpty()) {
                    return null;
                }
                
                for (T constant : enumType.getEnumConstants()) {
                    if (constant.name().equalsIgnoreCase(source)) {
                        return constant;
                    }
                }
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    String.format("%s enum tipi için geçersiz değer: %s", enumType.getSimpleName(), source));
            }
        }
    }
} 