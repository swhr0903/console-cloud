package com.cloud.console;

import com.cloud.console.po.User;
import com.cloud.console.vo.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.List;

/** Created by Frank on 2017/8/7. 扩展的UserDetails */
public class UserDetails extends User
    implements org.springframework.security.core.userdetails.UserDetails {

  private List<UserRole> roles;

  public UserDetails(User user, List<UserRole> roles) {
    super(user);
    this.roles = roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (roles == null || roles.size() < 1) {
      return AuthorityUtils.commaSeparatedStringToAuthorityList("");
    }
    StringBuilder commaBuilder = new StringBuilder();
    for (UserRole role : roles) {
      commaBuilder.append(role.getRole_code()).append(",");
    }
    String authorities = commaBuilder.substring(0, commaBuilder.length() - 1);
    return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
  }

  @Override
  public String getUsername() {
    return super.getUsername();
  }

  @Override
  public String getPassword() {
    return super.getPassword();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public List<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(List<UserRole> roles) {
    this.roles = roles;
  }
}
