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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class BlogController {
    @Autowired
    private IBlogService blogService;
    @Autowired
    private ICategoryService categoryService;

    @ModelAttribute("categories")
    public List<Category> setUpCategory() {
        return (List<Category>) categoryService.findAll();
    }


    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/blog/create");
        modelAndView.addObject("blog", new Blog());
        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView saveBlog(@ModelAttribute("blog") Blog blog) {
        ModelAndView modelAndView = new ModelAndView("/blog/create");
        blog.setDate(new Date());
        blogService.save(blog);
        modelAndView.addObject("blog", blog);
        modelAndView.addObject("message", "Blog created");
        return modelAndView;
    }
    @GetMapping("/api/blogs")
    public ResponseEntity<Iterable<Blog>> findAllBlogs(){
        List<Blog> blogs = (List<Blog>) blogService.findAll();
        if(blogs.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(blogs, HttpStatus.OK);
        }
    }

    @GetMapping("/blogs")
    //Toi da 6 blog/trang, sap xep theo cot date tang dan
    public ModelAndView showBlogs(@RequestParam("q") Optional<String> q, @PageableDefault(size = 6, sort = "date") Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/blog/menu");
        Page<Blog> blogs;
        if (q.isPresent()) {
            blogs = blogService.findAllByDescriptionContaining(q.get(), pageable);
        } else {
            blogs = blogService.findAll(pageable);
        }
        modelAndView.addObject("blogs", blogs);
        return modelAndView;
    }
    @GetMapping("/api/blogs/{id}")
    public ResponseEntity<Blog> findById(@PathVariable("id") Long id){
        Optional<Blog> blog = blogService.findById(id);
        if(!blog.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(blog.get(),HttpStatus.OK);
        }

    }
    @GetMapping("/{id}")
    public ModelAndView showDetail(@PathVariable("id") Long id) {
        Optional<Blog> blog = blogService.findById(id);
        Blog blog1 = blog.get();
        blog1.getCategory().getId();
        if (!blog.isPresent()) {
            return new ModelAndView("/error-404");
        } else {
            return new ModelAndView("/blog/view", "blog", blog.get());
        }

    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        Optional<Blog> blog = blogService.findById(id);
        if (!blog.isPresent()) {
            return new ModelAndView("/error-404");
        } else {
            return new ModelAndView("/blog/edit", "blog", blog.get());
        }
    }

    @PostMapping("/edit")
    public ModelAndView editBlog(@ModelAttribute Blog blog) {
        ModelAndView modelAndView = new ModelAndView("/blog/edit");
        blogService.save(blog);
        modelAndView.addObject("blog", blog);
        modelAndView.addObject("message", "Blog edited successfully");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable("id") Long id) {
        Optional<Blog> blog = blogService.findById(id);
        if (!blog.isPresent()) {
            return new ModelAndView("/error-404");
        } else {
            return new ModelAndView("/blog/delete", "blog", blog.get());
        }
    }

    @PostMapping("/delete")
    public String deleteBlog(@ModelAttribute("blog") Blog blog) {
        blogService.remove(blog.getId());
        return "redirect:/blogs";
    }
}