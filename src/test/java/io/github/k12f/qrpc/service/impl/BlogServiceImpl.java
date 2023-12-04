package io.github.k12f.qrpc.service.impl;


import io.github.k12f.qrpc.model.Blog;
import io.github.k12f.qrpc.service.BlogService;

public class BlogServiceImpl implements BlogService {
    @Override
    public Blog getBlogById(Integer id) {
        var blog = new Blog(id, 22, "blog zzzz");
        System.out.println("客户端查询了" + id + "博客");
        return blog;
    }
}
