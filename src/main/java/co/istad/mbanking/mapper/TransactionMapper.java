package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.Transaction;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "owner.actNo", target = "actNoOfOwner")
    @Mapping(source = "receiver.actNo", target = "actNoOfReceiver")
    @Mapping(source = "paymentReceiver", target = "paymentReceiver")
    TransactionResponse toTransactionResponse(Transaction transaction);

}
