//package com.kshrd.devconnect_springboot.exception;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.time.Instant;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE + 1)
//public class GlobalExceptionFilter extends OncePerRequestFilter {
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    protected void doFilterInternal(@NotNull HttpServletRequest request,
//                                    @NotNull HttpServletResponse response,
//                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
//        try {
//            filterChain.doFilter(request, response);
//        } catch (Exception ex) {
//            if (!response.isCommitted()) {
//
//                int status = resolveHttpStatus(ex);
//
//                Map<String, Object> body = new LinkedHashMap<>();
//                body.put("timestamp", Instant.now().toString());
//                body.put("status", status);
//                body.put("error", getErrorTitle(ex));
//                body.put("message", getErrorMessage(ex));
//                body.put("path", request.getRequestURI());
//
//                response.setStatus(status);
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                objectMapper.writeValue(response.getOutputStream(), body);
//            } else {
//                throw ex;
//            }
//        }
//    }
//
//    private int resolveHttpStatus(Exception ex) {
//        if (ex instanceof org.springframework.dao.DataAccessException) {
//            return HttpStatus.INTERNAL_SERVER_ERROR.value();
//        } else if (ex instanceof IllegalArgumentException ||
//                ex instanceof org.springframework.web.bind.MissingServletRequestParameterException) {
//            return HttpStatus.BAD_REQUEST.value();
//        } else {
//            return HttpStatus.INTERNAL_SERVER_ERROR.value();
//        }
//    }
//
//    private String getErrorTitle(Exception ex) {
//        return switch (ex) {
//            case org.springframework.dao.DataAccessException dataAccessException -> "Database Error";
//            case IllegalArgumentException illegalArgumentException -> "Invalid Request";
//            case org.springframework.web.bind.MissingServletRequestParameterException missingServletRequestParameterException ->
//                    "Missing Parameter";
//            case null, default -> "Unexpected Error";
//        };
//    }
//
//    private String getErrorMessage(Exception ex) {
//        if (ex instanceof org.springframework.dao.DataAccessException) {
//            return "A database error occurred. Please try again later.";
//        } else if (ex instanceof IllegalArgumentException ||
//                ex instanceof org.springframework.web.bind.MissingServletRequestParameterException) {
//            return ex.getMessage();
//        } else {
//            return "Something went wrong. Please try again later.";
//        }
//    }
//
//}
