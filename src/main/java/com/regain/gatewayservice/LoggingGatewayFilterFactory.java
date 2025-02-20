package com.regain.gatewayservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("Logging")
public class LoggingGatewayFilterFactory extends AbstractGatewayFilterFactory<LoggingGatewayFilterFactory.Config> {
    final Logger logger = LoggerFactory.getLogger(LoggingGatewayFilterFactory.class);

    public LoggingGatewayFilterFactory(Class<Config> configClass) {
        super(configClass);
    }

    @Override
    public GatewayFilter apply(LoggingGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            logger.info("Pre Gateway Filter logging: ");
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        logger.info("Post Gateway Filter logging: ");
                    }));
        };
    }

    public static class Config {
        private String baseMsg;

        public String getBaseMsg() {
            return baseMsg;
        }

        public void setBaseMsg(String baseMsg) {
            this.baseMsg = baseMsg;
        }
    }
}
