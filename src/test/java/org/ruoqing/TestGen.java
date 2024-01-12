package org.ruoqing;


import org.junit.Test;
import org.ruoqing.util.JdbcUtil;

public class TestGen {
    @Test
    public void test() {
        SwingGen.go(JdbcUtil.URL, JdbcUtil.USERNAME, JdbcUtil.PASSWORD)
                .packageConfig(packageConfigBuilder -> packageConfigBuilder.parent("org.ruoqing.code"))
                .entityConfig(strategyConfigBuilder -> strategyConfigBuilder.tableName("stu"))
                .swingConfig(swingConfigBuilder -> swingConfigBuilder.needLogin().title("学生"))
                .gen();
    }
}
