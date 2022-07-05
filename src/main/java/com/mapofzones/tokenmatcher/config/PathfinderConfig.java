package com.mapofzones.tokenmatcher.config;

import com.mapofzones.tokenmatcher.common.properties.PathfinderProperties;
import com.mapofzones.tokenmatcher.common.threads.IThreadStarter;
import com.mapofzones.tokenmatcher.common.threads.ThreadStarter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class PathfinderConfig {

    @Bean
    public PathfinderProperties pathfinderProperties() {
        return new PathfinderProperties();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public IThreadStarter pathfinderThreadStarter() {
        return new ThreadStarter(pathfinderProperties().getThreads());
    }
}
