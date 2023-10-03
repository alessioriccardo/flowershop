package icare.interview.codingTest.repositories;

import icare.interview.codingTest.models.Bundle;
import icare.interview.codingTest.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BundleRepository extends JpaRepository<Bundle, Long> {

    @Query("SELECT b FROM Bundle b JOIN FETCH b.productCode WHERE b.productCode.code=?1")
    List<Bundle> findByCode(String code);
}