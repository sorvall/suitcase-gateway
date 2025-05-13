package ru.sorokin.gateway.configuration;

import io.netty.channel.ChannelOption;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.server.HttpServer;

@Configuration
public class NettyHttpConfig {

    @Bean
    public WebServerFactoryCustomizer<NettyReactiveWebServerFactory> nettyCustomizer() {
        return factory -> factory.addServerCustomizers(httpServer -> {
            return HttpServer.create()
                    .port(80)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .route(routes -> routes.route(req -> true, (request, response) -> {
                        String host = request.requestHeaders().get("Host");
                        return response.status(301)
                                .header("Location", "https://" + host + request.uri())
                                .send();
                    }));
        });
    }
}
