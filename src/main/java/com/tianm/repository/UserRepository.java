package com.tianm.repository;

import com.tianm.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SunYi on 2016/3/24/0024.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByName(String name);
}
