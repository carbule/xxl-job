package com.korant.youya.workplace.exception;

import com.korant.youya.workplace.pojo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @author chenyiqiang
 * @date 2023-07-18
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({YouyaException.class})
    public ResponseEntity<?> handleBizException(YouyaException e) {
        log.error("业务发生异常，异常信息：", e);
        HttpStatus httpStatus = HttpStatus.valueOf(e.getStatusCode());
        return ResponseEntity.status(httpStatus).body(R.error(e.getMessage()));
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> handleException(Exception e) {
        log.error("系统发生异常,异常信息：", e);
        return R.error("系统正在开小差，请稍后再试");
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public R<?> handleHttpRequestMethodNotSupportedException(Exception e) {
        log.error("HTTP请求方式不被支持：", e);
        return R.error(e.getMessage());
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R<?> handleNoHandlerFoundException(Exception e) {
        log.error("HTTP请求内容未找到：", e);
        return R.error("HTTP请求内容未找到:" + e.getMessage());
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> handleBindException(Exception e) {
        log.error("请求参数不合法:", e);
        return R.error(((BindException) e).getBindingResult().getAllErrors().stream().findFirst().map(DefaultMessageSourceResolvable::getDefaultMessage).orElse("参数不合法"));
    }
}
