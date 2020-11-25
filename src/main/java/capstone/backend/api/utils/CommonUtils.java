package capstone.backend.api.utils;

import capstone.backend.api.entity.KeyResult;
import capstone.backend.api.entity.Role;
import capstone.backend.api.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@AllArgsConstructor
public class CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    public static final String PATTERN_ddMMyyyy = "dd/MM/yyyy";

    private final RoleRepository roleRepository;

    public static final String ROLE_USER = "ROLE_USER";

    public Date stringToDate(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.parse(dateStr);
    }

    public Set<Role> getUserRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role role = roleRepository.findRoleByName(CommonUtils.ROLE_USER).get();
            logger.info("add role USER");
            roles.add(role);
        } else {
            strRoles.forEach(role -> {
                Role roleDb = roleRepository.findRoleByName(role).orElse(null);
                if (roleDb != null) {
                    logger.info("add role " + role);
                    roles.add(roleDb);
                }
            });
        }
        return roles;
    }

    public String generateRandomCode(int codeSize) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(codeSize);

        for (int i = 0; i < codeSize; i++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public ArrayList<Long> stringToArray(String string) {
        ArrayList<Long> longArray = new ArrayList<>();
        if (string != null && !string.trim().isEmpty()) {
            String[] array = string.split(",");
            for (String item : array) {
                if (!item.trim().isEmpty()) {
                    longArray.add(Long.parseLong(item.trim()));
                }
            }
        }
        return longArray;
    }

    public String arrayToString(List<Long> longArray) {
        StringBuilder string = new StringBuilder(",");
        for (Long along : longArray) {
            string.append(along).append(",");
        }
        return string.toString();
    }

    public Map<String,Integer> paging(Page<?> page,int currentPage){

        Map<String,Integer> meta = new HashMap<>();
        meta.put("totalItems", (int) page.getTotalElements());
        meta.put("totalPages",page.getTotalPages());
        meta.put("currentPage",currentPage);

        return meta;
    }
}
