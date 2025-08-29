package co.istad.mbanking.features.branch;

import co.istad.mbanking.domain.Branch;
import co.istad.mbanking.features.branch.dto.BranchRequest;
import co.istad.mbanking.features.branch.dto.BranchResponse;
import co.istad.mbanking.mapper.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Override
    public List<BranchResponse> findAll() {
        // Get all non-deleted branches
        List<Branch> branches = branchRepository.findByIsDeletedFalse();
        return branchMapper.toBranchResponses(branches);
    }

    @Override
    public BranchResponse findById(Integer id) {
        // Find branch by ID and ensure it's not deleted
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(id);
        if (branch == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Branch with ID " + id + " not found"
            );
        }
        return branchMapper.toBranchResponse(branch);
    }

    @Override
    @Transactional
    public BranchResponse create(BranchRequest branchRequest) {
        // Map the request to entity and save
        Branch branch = branchMapper.fromBranchRequest(branchRequest);
        branch = branchRepository.save(branch);
        return branchMapper.toBranchResponse(branch);
    }

    @Override
    @Transactional
    public BranchResponse updateById(Integer id, BranchRequest branchRequest) {
        // Find branch by ID and ensure it's not deleted
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(id);
        if (branch == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Branch with ID " + id + " not found"
            );
        }

        // Update branch from request
        branchMapper.updateBranchFromRequest(branchRequest, branch);
        branch = branchRepository.save(branch);
        return branchMapper.toBranchResponse(branch);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        // Find branch by ID and ensure it's not deleted
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(id);
        if (branch == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Branch with ID " + id + " not found"
            );
        }

        // Soft delete by setting isDeleted to true
        branch.setIsDeleted(true);
        branch.setUpdatedAt(LocalDateTime.now());
        branchRepository.save(branch);
    }
}
