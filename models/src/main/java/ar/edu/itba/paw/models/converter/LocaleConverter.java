package ar.edu.itba.paw.models.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Locale;

@Converter(autoApply = true)
public class LocaleConverter implements AttributeConverter<Locale, String> {
    @Override
    public String convertToDatabaseColumn(Locale locale) {
        return locale.toString();
    }

    @Override
    public Locale convertToEntityAttribute(String s) {
        return Locale.forLanguageTag(s);
    }
}
