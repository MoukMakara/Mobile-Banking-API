package co.istad.mbanking.features.branch;

import co.istad.mbanking.features.branch.dto.BranchRequest;
import co.istad.mbanking.features.branch.dto.BranchResponse;

import java.util.List;

public interface BranchService {
    // Get all branches
    List<BranchResponse> findAll();

    // Get branch by ID
    BranchResponse findById(Integer id);

    // Create new branch
    BranchResponse create(BranchRequest branchRequest);

    // Update branch
    BranchResponse updateById(Integer id, BranchRequest branchRequest);

    // Delete branch (soft delete)
    void deleteById(Integer id);
}
