package co.istad.mbanking.features.branch;

import co.istad.mbanking.exception.ApiResponse;
import co.istad.mbanking.features.branch.dto.BranchRequest;
import co.istad.mbanking.features.branch.dto.BranchResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    // List all branches - accessible to all authenticated users
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'CUSTOMER', 'STAFF', 'MANAGER', 'ADMIN')")
    public ApiResponse<List<BranchResponse>> findAll() {
        List<BranchResponse> branches = branchService.findAll();
        return ApiResponse.<List<BranchResponse>>builder()
                .success(true)
                .message("Branches retrieved successfully")
                .status(HttpStatus.OK)
                .payload(branches)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Get branch details by ID - accessible to all authenticated users
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'CUSTOMER', 'STAFF', 'MANAGER', 'ADMIN')")
    public ApiResponse<BranchResponse> findById(@PathVariable Integer id) {
        BranchResponse branch = branchService.findById(id);
        return ApiResponse.<BranchResponse>builder()
                .success(true)
                .message("Branch retrieved successfully")
                .status(HttpStatus.OK)
                .payload(branch)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Create a new branch - accessible to managers and admins only
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ApiResponse<BranchResponse> create(@RequestBody @Valid BranchRequest request) {
        BranchResponse branch = branchService.create(request);
        return ApiResponse.<BranchResponse>builder()
                .success(true)
                .message("Branch created successfully")
                .status(HttpStatus.CREATED)
                .payload(branch)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Update a branch - accessible to managers and admins only
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ApiResponse<BranchResponse> updateById(@PathVariable Integer id,
                                                 @RequestBody @Valid BranchRequest request) {
        BranchResponse branch = branchService.updateById(id, request);
        return ApiResponse.<BranchResponse>builder()
                .success(true)
                .message("Branch updated successfully")
                .status(HttpStatus.OK)
                .payload(branch)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Delete a branch (soft delete) - accessible to admins only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> deleteById(@PathVariable Integer id) {
        branchService.deleteById(id);
        return ApiResponse.builder()
                .success(true)
                .message("Branch deleted successfully")
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
