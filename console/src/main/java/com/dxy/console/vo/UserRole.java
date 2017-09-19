package com.dxy.console.vo;

import lombok.Data;

/**
 * Created by Frank on 2017/8/7.
 */
@Data
public class UserRole {

    private static final String ROLE_PREFIX = "ROLE_";

    private Long id;
    private Long user_id;
    private Long role_id;
    private String role_code;
    private String role_name;

    public void setRole_code(String role_code) {
        this.role_code = ROLE_PREFIX + role_code;
    }
}
