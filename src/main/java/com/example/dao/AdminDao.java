package com.example.dao;

import com.example.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by SunYi on 2016/2/1/0001.
 */


@Repository
public interface AdminDao extends JpaRepository<Admin, Long> {

    Admin getByUsername(String username);
}
