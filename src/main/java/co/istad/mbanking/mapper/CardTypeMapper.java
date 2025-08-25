package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.CardType;
import co.istad.mbanking.features.card.dto.CardTypeRequest;
import co.istad.mbanking.features.card.dto.CardTypeResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CardTypeMapper {
    // Convert from entity to dto
    CardTypeResponse toCardTypeResponse(CardType cardType);

    // Convert from request DTO to entity
    CardType fromCardTypeRequest(CardTypeRequest cardTypeRequest);

    // Update entity partially (ignore null fields)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCardTypeFromRequest(CardTypeRequest cardTypeRequest, @MappingTarget CardType cardType);
}
