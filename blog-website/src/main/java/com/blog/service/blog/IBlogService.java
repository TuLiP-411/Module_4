package com.blog.service.blog;


import com.blog.model.Blog;
import com.blog.model.Category;
import com.blog.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBlogService extends IGeneralService<Blog> {
    Page<Blog> findAll(Pageable pageable);
    Page<Blog> findAllByDescriptionContaining(String description, Pageable pageable);
    Page<Blog> findAllByCategory(Category category, Pageable pageable);
    Iterable<Blog> findAllByCategory(Category category);
}
