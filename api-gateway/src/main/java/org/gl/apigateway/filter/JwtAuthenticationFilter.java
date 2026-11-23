package org.gl.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.gl.apigateway.service.JwtValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component

public class JwtAuthenticationFilter implements GlobalFilter , Ordered {


    public static final List<String> PUBLIC_PATH = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/actuator/health"
    );
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtValidator jwtValidator;

    public JwtAuthenticationFilter(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();



        boolean isPublicPath = PUBLIC_PATH.stream().anyMatch(path::startsWith);

        if (isPublicPath){
            return chain.filter(exchange);
        }

        String header = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (header==null || !header.startsWith("Bearer ")){
            exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
            exchange.getResponse().getHeaders().add("X-Auth-Error","Unauthorized");
            return exchange.getResponse().setComplete();
        }

        String token = header.substring(7);

        try {
            Claims claims = jwtValidator.validateJwt(token);

            ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate()
                    .header("X-User-ID", claims.getSubject())
                    .header("X-User-Role", claims.get("role", String.class));

            String email = claims.get("email", String.class);
            if (email != null) {
                requestBuilder.header("X-User-Email", email);
            }

            ServerHttpRequest serverHttpRequest = requestBuilder.build();

            ServerWebExchange webExchange = exchange.mutate().request(serverHttpRequest).build();

            return chain.filter(webExchange);
        }catch (JwtException ex) {
            log.warn("JWT validation failed: {}", ex.getMessage());

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
