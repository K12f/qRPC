package io.github.k12f.qrpc.model;

import java.io.Serializable;

public record Blog(
        Integer id,
        Integer userId,
        String title
) implements Serializable {
}
