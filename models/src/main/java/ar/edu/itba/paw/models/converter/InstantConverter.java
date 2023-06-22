package ar.edu.itba.paw.models.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Converter(autoApply = true)
public class InstantConverter implements AttributeConverter<Instant, Timestamp>  {
    @Override
    public Timestamp convertToDatabaseColumn(Instant attribute) {
        return Timestamp.from(attribute);
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp dbData) {
        return dbData.toInstant();
    }
}
