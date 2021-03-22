package com.offcn.service;

import java.util.ArrayList;
import java.util.List;

import com.offcn.pojo.TbSeller;
import com.offcn.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by travelround on 2019/1/26.
 */
// 认证类
// UserDetailsService为系统提供的接口,非自定义
public class UserDetailsServiceImpl implements UserDetailsService {
    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /*List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        return new User(username, "123456", grantedAuths);*/

        System.out.println("经过了UserDetailsServiceImpl");
        //构建角色列表
        List<GrantedAuthority> grantAuths = new ArrayList();
        grantAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        //得到商家对象
        TbSeller seller = sellerService.findOne(username);
        if (seller != null) {
            if (seller.getStatus().equals("1")) {
                return new User(username, seller.getPassword(), grantAuths);
            } else {
                return null;
            }
        } else {
            return null;
        }

    }
}