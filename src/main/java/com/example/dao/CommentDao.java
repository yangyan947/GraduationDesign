package com.example.dao;

import com.example.domain.Blog;
import com.example.domain.Comment;
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
public interface CommentDao extends JpaRepository<Comment, Long> {
    // 根据username获得一个User类
    Page<Comment> findAll(Pageable pageable);
    Page<Comment> getByStatus(String status,Pageable pageable);
    Page<Comment> getByBlog(Blog blog,Pageable pageable);
    Page<Comment> getByUser(User user, Pageable pageable);
    Page<Comment> getByContextLike(String fragment, Pageable pageable);


}
