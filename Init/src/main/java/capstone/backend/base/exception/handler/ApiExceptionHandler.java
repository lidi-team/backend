package capstone.backend.base.exception.handler;

import capstone.backend.base.exception.BadRequestException;
import capstone.backend.base.exception.ForbiddenException;
import capstone.backend.base.exception.InternalServerException;
import capstone.backend.base.exception.UnauthorizedException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * define returned httpStatus when throw exception
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * logger
     */
    private Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * used for all type of exception thrown
     *
     * @param e
     * @return ""
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String allExceptionHandler(Exception e) {
        logger.warn(e.getMessage());
        return StringUtils.EMPTY;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public String BadRequestExceptionHandler(BadRequestException e) {
        logger.warn(e.getMessage());
        return StringUtils.EMPTY;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    @ResponseBody
    public String ForbiddenExceptionHandler(ForbiddenException e) {
        logger.warn(e.getMessage());
        return StringUtils.EMPTY;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerException.class)
    @ResponseBody
    public String InternalServerHandler(InternalServerException e) {
        logger.warn(e.getMessage());
        return StringUtils.EMPTY;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public String UnauthorizedHandler(UnauthorizedException e) {
        logger.warn(e.getMessage());
        return StringUtils.EMPTY;
    }
}
