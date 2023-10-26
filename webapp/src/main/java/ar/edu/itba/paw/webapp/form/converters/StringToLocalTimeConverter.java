package ar.edu.itba.paw.webapp.form.converters;

import org.springframework.core.convert.converter.Converter;
import java.time.LocalTime;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String source) {
        return LocalTime.parse(source);
    }
}