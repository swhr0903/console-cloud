package com.cloud.console.mapper;

import com.cloud.console.po.Module;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/8/3.
 */
public interface IndexMapper {

    @Select("select id,name,url,parent_id,is_leaf,status,options from t_module where status=1 order by id")
    List<Module> getModules();

    @Select("select paytyple,count(1) as num from a_pay_order where " +
            "((paytyple=0 and paymethods=0) || paytyple=1 || paytyple=2 || paytyple=3)  and status=1 " +
            "group by paytyple")
    List<Map<Integer, Integer>> getDWCount();
}
