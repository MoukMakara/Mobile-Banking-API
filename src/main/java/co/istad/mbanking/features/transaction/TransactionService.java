package co.istad.mbanking.features.transaction;

import co.istad.mbanking.features.account.dto.AccountDetailResponse;
import co.istad.mbanking.features.account.dto.DepositRequest;
import co.istad.mbanking.features.account.dto.WithdrawRequest;
import co.istad.mbanking.features.transaction.dto.PaymentRequest;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import co.istad.mbanking.features.transaction.dto.TransferRequest;
import org.springframework.security.core.Authentication;

public interface TransactionService {

    TransactionResponse transfer(TransferRequest transferRequest, Authentication auth);

    TransactionResponse payment(PaymentRequest paymentRequest, Authentication auth);

    AccountDetailResponse deposit(String actNo, DepositRequest depositRequest);

    AccountDetailResponse withdraw(String actNo, WithdrawRequest withdrawRequest);
}
