package ar.edu.itba.paw.models.converter;

import ar.edu.itba.paw.models.enums.TimeDemanding;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TimeDemandingConverter implements AttributeConverter<TimeDemanding, Long> {
    @Override
    public Long convertToDatabaseColumn(TimeDemanding timeDemanding) {
        return timeDemanding.getValue();
    }

    @Override
    public TimeDemanding convertToEntityAttribute(Long value) {
        return TimeDemanding.parse(value);
    }
}
