package com.capstone.kots.repository;

import com.capstone.kots.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByUsername(String userName);
    User findByAccessToken(String accessToken);
    @Override
    List<User> findAll();

    Optional<User> findById(int id);

    void delete(User user);


}
