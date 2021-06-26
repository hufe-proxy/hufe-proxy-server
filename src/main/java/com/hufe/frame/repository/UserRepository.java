package com.hufe.frame.repository;

import com.hufe.frame.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.account = :account and u.password =:password and u.roleType =:roleType and u.isActive = true")
    UserEntity findOne(@Param("account") String account, @Param("password") String password, @Param("roleType") int roleType);

}
