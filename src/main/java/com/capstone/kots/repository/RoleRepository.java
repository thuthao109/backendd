package com.capstone.kots.repository;

import com.capstone.kots.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {


    @Query(value="SELECT r.role_name FROM roles r " +
            "LEFT JOIN users u on u.role_id = r.id " +
            "WHERE u.id = :userId",nativeQuery = true)
    List<String> getRolesNames(@Param("userId") Integer userId);

    Optional<Role> findRolesByRoleName(String roleName);


}
