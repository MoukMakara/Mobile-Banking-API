package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.Branch;
import co.istad.mbanking.features.branch.dto.BranchRequest;
import co.istad.mbanking.features.branch.dto.BranchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    BranchResponse toBranchResponse(Branch branch);
    List<BranchResponse> toBranchResponses(List<Branch> branches);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "users", ignore = true)
    Branch fromBranchRequest(BranchRequest branchRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "accounts", ignore = true)
    @Mapping(target = "users", ignore = true)
    void updateBranchFromRequest(BranchRequest request, @MappingTarget Branch branch);
}
