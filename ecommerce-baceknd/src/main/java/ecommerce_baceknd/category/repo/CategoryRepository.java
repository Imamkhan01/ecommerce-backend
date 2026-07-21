package ecommerce_baceknd.category.repo;

import ecommerce_baceknd.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySlug(String slug);

    boolean existsByNameIgnoreCase(String name);

    boolean existsBySlug(String slug);

    Page<Category> findByActiveTrue(Pageable pageable);

    Optional<Category> findByIdAndActiveTrue(Long id);

    Optional<Category> findBySlugAndActiveTrue(String slug);
}
