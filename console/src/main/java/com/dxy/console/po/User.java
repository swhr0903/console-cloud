package com.dxy.console.po;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Frank on 2017/8/3.
 */
@Data
public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private Timestamp create_time;
    private String avatar;
    private Integer status;

    public User(){

    }

    public User(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.email = user.getEmail();
        this.create_time = user.getCreate_time();
        this.status = user.getStatus();
    }
}
