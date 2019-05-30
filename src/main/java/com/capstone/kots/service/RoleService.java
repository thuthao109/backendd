package com.capstone.kots.service;


import com.capstone.kots.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<String> getRoleName(Integer userId){
        log.info("Call getRoleName method with userId is "+userId);
        return roleRepository.getRolesNames(userId);
    }
}
