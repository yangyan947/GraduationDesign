package com.example.dao;

import com.example.domain.Blog;
import com.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by SunYi on 2016/2/1/0001.
 */


@Repository
public interface BlogDao extends JpaRepository<Blog, Long> {

    Optional<Blog> getByTitle(String title);

    List<Blog> getByUser(User user);

    @Query("select b from Blog b where b.user in ?1")
    Page<Blog> getByUsers(List<User> user, Pageable pageable);

    Page<Blog> findAll(Pageable pageable);
}
