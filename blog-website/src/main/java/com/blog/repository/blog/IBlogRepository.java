package com.blog.repository.blog;

import com.blog.model.Blog;
import com.blog.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBlogRepository extends PagingAndSortingRepository<Blog, Long> {
    Page<Blog> findAllByDescriptionContaining(String description, Pageable pageable);
    Page<Blog> findAllByCategory(Category category, Pageable pageable);
    Iterable<Blog> findAllByCategory(Category category);
}