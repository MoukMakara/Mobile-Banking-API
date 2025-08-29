package co.istad.mbanking.features.branch;

import co.istad.mbanking.domain.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    List<Branch> findByIsDeletedFalse();
    Branch findByIdAndIsDeletedFalse(Integer id);
}
