package com.example.dao;

import com.example.domain.Blog;
import com.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by SunYi on 2016/2/1/0001.
 */


@Repository
public interface BlogDao extends JpaRepository<Blog, Long> {


    Page<Blog> getByUserOrderByCreateTimeDesc(User user, Pageable pageable);

    Page<Blog> getByUser(User user, Pageable pageable);

    @Query("select b from Blog b where b.user in ?1")
    Page<Blog> getByUsers(Set<User> user, Pageable pageable);

    Page<Blog> getByUserInAndStatusIsNot(Set<User> user,String status, Pageable pageable);

    Page<Blog> findAll(Pageable pageable);
    Page<Blog> getByStatus(String status,Pageable pageable);
    Page<Blog> getByStatusIsNot(String status,Pageable pageable);

    Page<Blog> getByContextLike(String fragment, Pageable pageable);
}
