package co.istad.mbanking.features.transaction;

import co.istad.mbanking.base.BasedTransactionType;
import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.Transaction;
import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.account.AccountRepository;
import co.istad.mbanking.features.account.dto.AccountDetailResponse;
import co.istad.mbanking.features.account.dto.DepositRequest;
import co.istad.mbanking.features.account.dto.WithdrawRequest;
import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import co.istad.mbanking.features.transaction.dto.TransferRequest;
import co.istad.mbanking.mapper.AccountMapper;
import co.istad.mbanking.mapper.TransactionMapper;
import co.istad.mbanking.security.CurrentUserUtil;
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
    private final AccountMapper accountMapper;
    private final CurrentUserUtil currentUserUtil;

    @Transactional
    @Override
    public TransactionResponse transfer(TransferRequest transferRequest, Authentication auth) {
        // Validate actNoOfOwner
        Account accountOwner = accountRepository
                .findByActNo(transferRequest.actNoOfOwner())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid account owner"));

        // Check if the source account belongs to the current authenticated user
        User currentUser = currentUserUtil.getCurrentUser();
        boolean isUserAccount = accountOwner.getUserAccount().getUser().getId().equals(currentUser.getId());

        if (!isUserAccount) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only transfer from your own accounts"
            );
        }

        // Validate actNoOfReceiver
        Account accountReceiver = accountRepository
                .findByActNo(transferRequest.actNoOfReceiver())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid account receiver"));

        // Prevent transfer to the same account
        if (accountOwner.getActNo().equals(accountReceiver.getActNo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot transfer to the same account"
            );
        }

        // Validate amount
        if (transferRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transfer amount must be greater than zero"
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

        // Subtract money from owner account
        BigDecimal latestBalanceOfOwner = accountOwner
                .getBalance()
                .subtract(transferRequest.amount());
        accountOwner.setBalance(latestBalanceOfOwner);

        // Add money to receiver account
        BigDecimal latestBalanceOfReceiver = accountReceiver
                .getBalance()
                .add(transferRequest.amount());
        accountReceiver.setBalance(latestBalanceOfReceiver);

        // Save both accounts
        accountRepository.save(accountOwner);
        accountRepository.save(accountReceiver);

        // Create transaction record
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

    @Transactional
    @Override
    public AccountDetailResponse deposit(String actNo, DepositRequest depositRequest) {
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account doesn't exist"
                ));

        // Check if the account belongs to the current authenticated user
        User currentUser = currentUserUtil.getCurrentUser();
        boolean isUserAccount = account.getUserAccount().getUser().getId().equals(currentUser.getId());

        if (!isUserAccount) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only deposit to your own accounts"
            );
        }

        BigDecimal total = account.getBalance().add(depositRequest.amount());
        account.setBalance(total);

        // Create a transaction record
        Transaction transaction = new Transaction();
        transaction.setReceiver(account);
        transaction.setOwner(account); // For deposit, owner and receiver are the same
        transaction.setAmount(depositRequest.amount());
        transaction.setRemark("Deposit to account");
        transaction.setTransactionAt(LocalDateTime.now());
        transaction.setStatus(true);
        transaction.setIsDeleted(false);
        transaction.setTransactionType(BasedTransactionType.DEPOSIT.toString());

        transactionRepository.save(transaction);
        Account savedAccount = accountRepository.save(account);

        return accountMapper.toAccountDetailResponse(savedAccount);
    }

    @Transactional
    @Override
    public AccountDetailResponse withdraw(String actNo, WithdrawRequest withdrawRequest) {
        Account account = accountRepository
                .findByActNo(actNo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account doesn't exist"
                ));

        // Check if the account belongs to the current authenticated user
        User currentUser = currentUserUtil.getCurrentUser();
        boolean isUserAccount = account.getUserAccount().getUser().getId().equals(currentUser.getId());

        if (!isUserAccount) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only withdraw from your own accounts"
            );
        }

        if (account.getBalance().compareTo(withdrawRequest.amount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Insufficient balance");
        }

        BigDecimal total = account.getBalance().subtract(withdrawRequest.amount());
        account.setBalance(total);

        // Create a transaction record
        Transaction transaction = new Transaction();
        transaction.setOwner(account);
        transaction.setReceiver(account); // For withdrawal, owner and receiver are the same
        transaction.setAmount(withdrawRequest.amount());
        transaction.setRemark("Withdrawal from account");
        transaction.setTransactionAt(LocalDateTime.now());
        transaction.setStatus(true);
        transaction.setIsDeleted(false);
        transaction.setTransactionType(BasedTransactionType.WITHDRAW.toString());

        transactionRepository.save(transaction);
        Account savedAccount = accountRepository.save(account);

        return accountMapper.toAccountDetailResponse(savedAccount);
    }
}
