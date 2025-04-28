package co.istad.mbanking.features.transaction;

import co.istad.mbanking.features.transaction.dto.TransactionResponse;
import co.istad.mbanking.features.transaction.dto.TransferRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/transfers")
    TransactionResponse transfer(@Valid @RequestBody TransferRequest transferRequest,
                                 Authentication auth) {
        return transactionService.transfer(transferRequest, auth);
    }


}
