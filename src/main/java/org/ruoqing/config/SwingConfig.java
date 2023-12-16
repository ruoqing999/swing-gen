package org.ruoqing.config;

import lombok.Data;

@Data
public class SwingConfig {

    //是否需要登录界面
    private boolean needLogin;

    //一般根据表名输入： 如 flower：鲜花 pet：宠物
    private String title;


    public static class Builder {
        private final SwingConfig swingConfig;

        public Builder() {
            this.swingConfig = new SwingConfig();
        }

        public Builder(SwingConfig swingConfig) {
            this.swingConfig = swingConfig;
        }

        public Builder needLogin() {
            this.swingConfig.needLogin = true;
            return this;
        }

        public Builder title(String title) {
            this.swingConfig.title = title;
            return this;
        }

        public SwingConfig build() {
            return this.swingConfig;
        }

    }

}
