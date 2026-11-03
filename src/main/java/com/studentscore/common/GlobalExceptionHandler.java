package com.studentscore.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理：AJAX 请求返回 JSON，页面请求返回错误页。
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public Object handleBusiness(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常：{}", e.getMessage());
        return handle(e.getMessage(), request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常", e);
        String msg = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
        return handle("系统处理失败：" + msg, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Object handle(String message, HttpServletRequest request, HttpStatus status) {
        if (isAjax(request)) {
            return new ResponseEntity<>(Result.fail(status.value(), message), HttpStatus.OK);
        }
        ModelAndView mv = new ModelAndView("error/500");
        mv.addObject("message", message);
        mv.setStatus(status);
        return mv;
    }

    private boolean isAjax(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        String accept = request.getHeader("Accept");
        return "XMLHttpRequest".equalsIgnoreCase(requestedWith)
                || (accept != null && accept.contains("application/json"));
    }
}
