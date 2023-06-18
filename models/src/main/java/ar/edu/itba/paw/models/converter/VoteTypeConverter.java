package ar.edu.itba.paw.models.converter;

import ar.edu.itba.paw.models.enums.ReviewVoteType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class VoteTypeConverter implements AttributeConverter<ReviewVoteType, Long> {

    @Override
    public Long convertToDatabaseColumn(ReviewVoteType reviewVoteType) {
        return reviewVoteType.getIntValue();
    }

    @Override
    public ReviewVoteType convertToEntityAttribute(Long value) {
        return ReviewVoteType.parse(value);
    }
}
