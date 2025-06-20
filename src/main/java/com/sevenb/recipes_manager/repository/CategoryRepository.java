package com.sevenb.recipes_manager.repository;

import com.sevenb.recipes_manager.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByDescriptionAndUserId(String name, Long userId);
    List<Category> findAllByUserId(Long userId);

}
