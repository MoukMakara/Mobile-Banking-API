package co.istad.mbanking.features.transaction;

import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import co.istad.mbanking.features.transaction.dto.TransferRequest;
import org.springframework.security.core.Authentication;

public interface TransactionService {

    TransactionResponse transfer(TransferRequest transferRequest, Authentication auth);


}
