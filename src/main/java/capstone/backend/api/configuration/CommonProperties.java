package capstone.backend.api.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:CommonProperties.properties")
@ConfigurationProperties(prefix = "information")
public class CommonProperties {

    private String secret;
    private Long expiration;
    private int codeSize;
    private int resetCodeSize;
    private Long expiredTime;
    private String MESSAGE_SUCCESS;
    private String MESSAGE_BAD_REQUEST;
    private String MESSAGE_AUTH_FAILED;
    private String MESSAGE_PARAM_VALUE_INVALID;
    private String MESSAGE_PARAM_TIME_OUT;
    private String MESSAGE_NOT_FOUND;
    private String MESSAGE_PARAM_VALUE_EMPTY;
    private String MESSAGE_UNDEFINE_ERROR;
    private String MESSAGE_PARAM_FORMAT_INVALID;
    private String MESSAGE_EXIST_VALUE;
    private int CODE_SUCCESS;
    private int CODE_BAD_REQUEST;
    private int CODE_AUTH_FAILED;
    private int CODE_PARAM_VALUE_INVALID;
    private int CODE_PARAM_TIME_OUT;
    private int CODE_NOT_FOUND;
    private int CODE_PARAM_VALUE_EMPTY;
    private int CODE_UNDEFINE_ERROR;
    private int CODE_PARAM_FORMAT_INVALID;
    private int CODE_EXIST_VALUE;
    private String EC_LEADER_TO_STAFF;
    private String EC_STAFF_TO_LEADER;
    private String OBJ_RUNNING;
    private String OBJ_FINISHED;
    private String OBJ_DRAFT;
    private String R_DRAFT;
    private String R_SUBMITTED;
    private String R_REJECTED;
    private String R_APPROVED;
    private int OBJ_COMPANY;
    private int OBJ_PROJECT;
    private int OBJ_PERSONAL;


}
