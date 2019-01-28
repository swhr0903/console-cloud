package com.cloud.console.mapper;

import org.apache.ibatis.annotations.Insert;

/** Created by Frank on 2019-01-18. */
public interface UserInfoMapper {
  @Insert("insert into t_user_info(id,account) values(#{id},#{account})")
  void insert(UserInfo userInfo);
}
