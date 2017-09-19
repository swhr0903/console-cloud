package com.dxy.console.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Frank on 2017/9/5.
 */
@Data
public class TreeNode {
    private String text;
    private String value;
    private List<TreeNode> nodes;
}
