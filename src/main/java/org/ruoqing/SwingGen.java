package org.ruoqing;

import org.ruoqing.config.JdbcConfig;
import org.ruoqing.config.PackageConfig;
import org.ruoqing.config.EntityConfig;
import org.ruoqing.config.SwingConfig;

import java.util.function.Consumer;

public class SwingGen {

    private final JdbcConfig.Builder jdbcConfigBuilder;

    private final PackageConfig.Builder packageConfigBuilder;

    private final EntityConfig.Builder strategyConfigBuilder;

    private final SwingConfig.Builder swingConfigBuilder;

    private SwingGen(JdbcConfig.Builder jdbcConfigBuilder) {
        this.jdbcConfigBuilder = jdbcConfigBuilder;
        this.packageConfigBuilder = new PackageConfig.Builder();
        this.strategyConfigBuilder = new EntityConfig.Builder();
        this.swingConfigBuilder = new SwingConfig.Builder();
    }

    public static SwingGen go(String url, String userName, String password) {
        return new SwingGen(new JdbcConfig.Builder().url(url).userName(userName).password(password));
    }

    public SwingGen packageConfig(Consumer<PackageConfig.Builder> consumer) {
        consumer.accept(this.packageConfigBuilder);
        return this;
    }

    public SwingGen entityConfig(Consumer<EntityConfig.Builder> consumer) {
        consumer.accept(this.strategyConfigBuilder);
        return this;
    }

    public SwingGen swingConfig(Consumer<SwingConfig.Builder> consumer) {
        consumer.accept(this.swingConfigBuilder);
        return this;
    }

    public void gen() {
        new AutoGen()
                .jdbcInfo(jdbcConfigBuilder.build())
                .packageInfo(packageConfigBuilder.build())
                .strategy(strategyConfigBuilder.build())
                .swingInfo(swingConfigBuilder.build())
                .gen();
    }


}
