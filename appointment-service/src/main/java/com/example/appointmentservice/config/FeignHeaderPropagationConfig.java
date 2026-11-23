package com.example.appointmentservice.config;


import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignHeaderPropagationConfig {

    private static final String[] PROPAGATED_HEADERS = {
            "X-User-Id", "X-User-Role", "X-User-Email", "Authorization"
    };

    @Bean
    public RequestInterceptor headerPropagationInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attrs == null) {
                return;
            }

            HttpServletRequest request = attrs.getRequest();
            for (String header : PROPAGATED_HEADERS) {
                String value = request.getHeader(header);
                if (value != null) {
                    requestTemplate.header(header, value);
                }
            }
        };
    }
}
