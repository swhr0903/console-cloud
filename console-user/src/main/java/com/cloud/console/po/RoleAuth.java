package com.cloud.console.po;

import lombok.Data;

/**
 * Created by Frank on 2017/9/4.
 */
@Data
public class RoleAuth {
    private Long id;
    private Long role_id;
    private Long module_id;
    private String permission;
}
