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
    public static final String ROLE_TEACHER = "ROLE_TEACHER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public Set<Role> getUserRoles(Set<String> strRoles){
        Set<Role> roles = new HashSet<>();

        if(strRoles == null){
            Role role = roleRepository.findRoleByName(RoleUtils.ROLE_USER).get();
            logger.info("add role USER");
            roles.add(role);
        } else{
            strRoles.forEach(role->{
                switch (role){
                    case RoleUtils.ROLE_USER:
                    {
                        Role roleDb = roleRepository.findRoleByName(RoleUtils.ROLE_USER).get();
                        logger.info("add role USER");
                        roles.add(roleDb);
                        break;
                    }
                    case RoleUtils.ROLE_TEACHER:
                    {
                        Role roleDb = roleRepository.findRoleByName(RoleUtils.ROLE_TEACHER).get();
                        logger.info("add role TEACHER");
                        roles.add(roleDb);
                        break;
                    }
                    case RoleUtils.ROLE_ADMIN:
                    {
                        Role roleDb = roleRepository.findRoleByName(RoleUtils.ROLE_ADMIN).get();
                        logger.info("add role ADMIN");
                        roles.add(roleDb);
                        break;
                    }
                }
            });
        }
        return roles;
    }
}
