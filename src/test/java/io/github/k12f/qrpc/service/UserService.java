package io.github.k12f.qrpc.service;


import io.github.k12f.qrpc.model.User;

/**
 *
 */
public interface UserService {
    User getUserByUserId(Integer id);

    // 插入数据
    Integer insertUserId(User user);
}
