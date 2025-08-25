package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.Card;
import co.istad.mbanking.features.card.dto.CardRequest;
import co.istad.mbanking.features.card.dto.CardResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CardMapper {
    // Convert from entity to dto
    CardResponse toCardResponse(Card card);

    // Convert from request DTO to entity
    Card fromCardRequest(CardRequest cardRequest);

    // Update entity partially (ignore null fields)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCardFromRequest(CardRequest cardRequest, @MappingTarget Card card);
}
