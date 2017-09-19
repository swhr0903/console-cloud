package com.dxy.console.po;

import lombok.Data;

/**
 * Created by Frank on 2017/8/4.
 */
@Data
public class Module {
    private Long id;
    private String name;
    private String url;
    private Long parent_id;
    private Integer is_leaf;
    private Integer status;
    private String options;
}
