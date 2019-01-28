package com.cloud.console.mapper;

import org.apache.ibatis.annotations.Insert;

/** Created by Frank on 2019-01-18. */
public interface PayOrderMapper {
  @Insert("insert into t_pay_order(id,poid,create_date) values(#{id},#{poid},#{createDate})")
  void insert(PayOrder payOrder);
}
