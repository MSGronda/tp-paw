package ar.edu.itba.paw.models.converter;

import ar.edu.itba.paw.models.enums.Difficulty;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DifficultyConverter implements AttributeConverter<Difficulty, Long> {
    @Override
    public Long convertToDatabaseColumn(Difficulty difficulty) {
        return difficulty.getValue();
    }

    @Override
    public Difficulty convertToEntityAttribute(Long value) {
        return Difficulty.parse(value);
    }
}
