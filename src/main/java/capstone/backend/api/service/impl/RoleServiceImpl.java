package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.Role;
import capstone.backend.api.repository.RoleRepository;
import capstone.backend.api.service.RoleService;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private RoleRepository roleRepository;

    private CommonProperties commonProperties;

    private JwtUtils jwtUtils;

    @Override
    public ResponseEntity<?> getAllRoles(String jwtToken) throws Exception {
        List<Role> roles = roleRepository.findAll();

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(roles).build()
        );
    }
}
