package com.dxy.console.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Frank on 2017/9/6.
 */
@Data
public class Auths {
    private Long roleId;
    private List<Permission> permissions;
}
