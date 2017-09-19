package com.dxy.console.service;

import java.util.List;

/**
 * Created by Frank on 2017/8/9.
 */
public class Paging {
    private Integer total;
    private List rows;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
