package org.ruoqing;


import org.junit.Test;
import org.ruoqing.util.JdbcUtil;

public class TestGen {

    @Test
    public void test() {
        SwingGen.go(JdbcUtil.URL, JdbcUtil.USERNAME, JdbcUtil.PASSWORD)
                .packageConfig(packageConfigBuilder -> packageConfigBuilder.parent("org.ruoqing"))
                .entityConfig(strategyConfigBuilder -> strategyConfigBuilder.tableName("flowers").enableLombok())
                .swingConfig(swingConfigBuilder -> swingConfigBuilder.needLogin().title("鲜花"))
                .gen();
    }


}
