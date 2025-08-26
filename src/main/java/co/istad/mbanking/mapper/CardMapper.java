package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.Card;
import co.istad.mbanking.features.card.dto.CardRequest;
import co.istad.mbanking.features.card.dto.CardResponse;
import co.istad.mbanking.features.card.dto.CardTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cardType", ignore = true)
    @Mapping(target = "issuedAt", ignore = true)
    @Mapping(target = "expiredAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "account", ignore = true)
    Card fromCardRequest(CardRequest cardRequest);

    @Mapping(target = "cardType", ignore = true)
    @Mapping(target = "issuedAt", ignore = true)
    @Mapping(target = "expiredAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "account", ignore = true)
    void updateCardFromRequest(CardRequest cardRequest, @MappingTarget Card card);

    @Mapping(target = "cardTypeId", source = "cardType.id")
    @Mapping(target = "cardTypeName", source = "cardType.name")
    @Mapping(target = "cardTypeAlias", source = "cardType.alias")
    @Mapping(target = "expiredAt", dateFormat = "yyyy-MM-dd")
    CardResponse toCardResponse(Card card);

    @Mapping(target = "description", source = "cardType.name") // Using name as description for now
    CardTypeResponse toCardTypeResponse(co.istad.mbanking.domain.CardType cardType);
}
