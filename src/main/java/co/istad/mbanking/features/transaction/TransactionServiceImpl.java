package co.istad.mbanking.features.transaction;

import co.istad.mbanking.base.BasedTransactionType;
import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.Transaction;
import co.istad.mbanking.features.account.AccountRepository;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import co.istad.mbanking.features.transaction.dto.TransferRequest;
import co.istad.mbanking.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    private final TransactionMapper transactionMapper;

    @Transactional
    @Override
    public TransactionResponse transfer(TransferRequest transferRequest, Authentication auth) {

        // Validate actNoOwner
        Account accountOwner = accountRepository
                .findByActNo(transferRequest.actNoOfOwner())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid account owner"));

        String userEmail = accountOwner.getUserAccount().getUser().getEmail();
        Jwt jwt = (Jwt) auth.getPrincipal();


        log.info("User Email: {}", userEmail);
        log.info("Auth Email: {}", jwt.getId());

        if (!userEmail.equals(jwt.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not authorized to perform this action"
            );
        }

        // Validate actNoReceiver
        Account accountReceiver = accountRepository
                .findByActNo(transferRequest.actNoOfReceiver())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid account receiver"));

        // Validate amount
        if (transferRequest.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transfer amount is invalid"
            );
        }

        // Validate insufficient balance
        if (transferRequest.amount().compareTo(accountOwner.getBalance()) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Insufficient balance"
            );
        }

        // Validate transfer limit
        if (transferRequest.amount().compareTo(accountOwner.getTransferLimit()) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transfer amount exceeds account limit"
            );
        }

        // ដកលុយចេញពី account owner
        BigDecimal latestBalanceOfOwner = accountOwner
                .getBalance()
                .subtract(transferRequest.amount());
        accountOwner.setBalance(latestBalanceOfOwner);

        // ដាក់លុយចូលទៅ account receiver
        BigDecimal latestBalanceOfReceiver = accountReceiver
                .getBalance()
                .add(transferRequest.amount());
        accountReceiver.setBalance(latestBalanceOfReceiver);

        Transaction transaction = new Transaction();
        transaction.setOwner(accountOwner);
        transaction.setReceiver(accountReceiver);
        transaction.setAmount(transferRequest.amount());
        transaction.setRemark(transferRequest.remark());
        transaction.setTransactionAt(LocalDateTime.now());
        transaction.setStatus(true);
        transaction.setIsDeleted(false);
        transaction.setTransactionType(BasedTransactionType.TRANSFER.toString());

        transaction = transactionRepository.save(transaction);

        return transactionMapper.toTransactionResponse(transaction);
    }

}
