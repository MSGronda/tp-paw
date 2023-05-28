package ar.edu.itba.paw.models.converter;

import ar.edu.itba.paw.models.enums.SubjectProgress;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SubjectProgressConverter implements AttributeConverter<SubjectProgress, Long> {
    @Override
    public Long convertToDatabaseColumn(SubjectProgress subjectProgress) {
        return subjectProgress.getValue();
    }

    @Override
    public SubjectProgress convertToEntityAttribute(Long value) {
        return SubjectProgress.parse(value);
    }
}
