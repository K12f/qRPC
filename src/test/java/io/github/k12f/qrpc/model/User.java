package io.github.k12f.qrpc.model;

import java.io.Serializable;

/**
 * @param id
 * @param username
 * @param sex
 */
public record User(Integer id, String username, Boolean sex) implements Serializable {
}
