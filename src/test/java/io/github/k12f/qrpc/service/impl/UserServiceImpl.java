package io.github.k12f.qrpc.service.impl;


import io.github.k12f.qrpc.model.User;
import io.github.k12f.qrpc.service.UserService;
import io.github.k12f.qrpc.util.LRUCache;

import java.util.Random;

public class UserServiceImpl implements UserService {
    private final LRUCache lruCache = new LRUCache(100);

    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("client querying id=" + id);
//        var random = new Random();
//        var user = new User(id,UUID.randomUUID().toString(),random.nextBoolean());
        return (User) lruCache.get(id);
    }

    @Override
    public Integer insertUserId(User user) {
        System.out.println("插入数据成功：" + user);
        var random = new Random();

        var id = random.nextInt(100000);
        lruCache.put(id, user);
        return id;
    }
}
