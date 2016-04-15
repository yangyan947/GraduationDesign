package com.example.dao;

import com.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by SunYi on 2016/2/1/0001.
 */
@Repository
//这个注解代表这是一个mybatis的操作数据库的类
public interface UserDao extends JpaRepository<User, Long> {
    // 根据username获得一个User类
    User getByEmail(String email);

    Page<User> findAll(Pageable pageable);

}
