package capstone.backend.api.utils;

import capstone.backend.api.entity.Role;
import capstone.backend.api.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class RoleUtils {
    private static final Logger logger = LoggerFactory.getLogger(RoleUtils.class);
    private RoleRepository roleRepository;

    public static final String ROLE_USER = "ROLE_USER";

    public Set<Role> getUserRoles(Set<String> strRoles){
        Set<Role> roles = new HashSet<>();

        if(strRoles == null){
            Role role = roleRepository.findRoleByName(RoleUtils.ROLE_USER).get();
            logger.info("add role USER");
            roles.add(role);
        } else{
            strRoles.forEach(role->{
                Role roleDb = roleRepository.findRoleByName(role).orElse(null);
                if(roleDb != null){
                    logger.info("add role "+role);
                    roles.add(roleDb);
                }
            });
        }
        return roles;
    }
}
