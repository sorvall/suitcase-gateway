package ru.sorokin.gateway.configuration;

import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyHttpConfig {
    @Bean
    public NettyServerCustomizer nettyServerCustomizer() {
        return httpServer -> httpServer
                .port(80)
                .handle((req, res) -> {
                    String host = req.requestHeaders().get("Host");
                    return res.status(301)
                            .header("Location", "https://" + host + req.uri())
                            .send();
                });
    }
}
