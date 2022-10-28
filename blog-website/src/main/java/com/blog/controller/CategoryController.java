package com.blog.controller;

import com.blog.model.Blog;
import com.blog.model.Category;
import com.blog.service.blog.IBlogService;
import com.blog.service.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IBlogService blogService;

    @GetMapping("/categories/view/{id}")
    public ModelAndView showBlogByCategory(@PathVariable("id") Long id, @PageableDefault(size = 6) Pageable pageable) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()) {
            return new ModelAndView("/error-404");
        } else {
            Page<Blog> blogs = blogService.findAllByCategory(categoryOptional.get(), pageable);
            ModelAndView modelAndView = new ModelAndView("/category/view");
            modelAndView.addObject("blogs", blogs);
            modelAndView.addObject("category", categoryOptional.get());
            return modelAndView;
        }
    }
    @GetMapping("/api/categories/view/{id}")
    public ResponseEntity<Iterable<Blog>> findBlogByCategory(@PathVariable("id") Long id){
        Optional<Category> categoryOptional= categoryService.findById(id);
        if(!categoryOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            List<Blog> blogs = (List<Blog>) blogService.findAllByCategory(categoryOptional.get());
            return new ResponseEntity<>(blogs, HttpStatus.OK);
        }
    }

    @GetMapping("/categories")
    public ModelAndView showList() {
        ModelAndView modelAndView = new ModelAndView("/category/menu");
        List<Category> categories = (List<Category>) categoryService.findAll();
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }
    @GetMapping("/api/categories")
    public ResponseEntity<Iterable<Category>> findAll(){
        List<Category> categories = (List<Category>) categoryService.findAll();
        if(categories.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
    }

    @GetMapping("/categories/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/category/create");
        modelAndView.addObject("category", new Category());
        return modelAndView;
    }

    @PostMapping("/categories/create")
    public ModelAndView createCategory(@ModelAttribute("category") Category category) {
        categoryService.save(category);
        ModelAndView modelAndView = new ModelAndView("/category/create");
        modelAndView.addObject("category", category);
        modelAndView.addObject("message", "Category created");
        return modelAndView;
    }

    @GetMapping("/categories/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            return new ModelAndView("/error-404");
        } else {
            return new ModelAndView("/category/edit", "category", category.get());
        }
    }

    @PostMapping("/categories/edit")
    public ModelAndView editCategory(@ModelAttribute("category") Category category) {
        ModelAndView modelAndView = new ModelAndView("/category/edit");
        categoryService.save(category);
        modelAndView.addObject("category", category);
        modelAndView.addObject("message", "Category updated");
        return modelAndView;
    }

    @GetMapping("/categories/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable("id") Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            return new ModelAndView("/error-404");
        } else {
            return new ModelAndView("/category/delete", "category", category.get());
        }
    }

    @PostMapping("/categories/delete")
    public String deleteCategory(@ModelAttribute("category") Category category) {
        Category category11 = category;
        categoryService.remove(category.getId());
        return "redirect:/categories";
    }


}
