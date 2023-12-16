package org.ruoqing.config;

import lombok.Data;



@Data
public class JdbcConfig {

    private String url;

    private String userName;

    private String password;

    public static class Builder {
        private JdbcConfig jdbcConfig;

        public Builder() {
            this.jdbcConfig = new JdbcConfig();
        }

        public Builder(JdbcConfig jdbcConfig) {
            this.jdbcConfig = jdbcConfig;
        }

        public Builder url(String url){
            this.jdbcConfig.url = url;
            return this;
        }

        public Builder userName(String userName){
            this.jdbcConfig.userName = userName;
            return this;
        }

        public Builder password(String password){
            this.jdbcConfig.password = password;
            return this;
        }

        public JdbcConfig build(){
            return this.jdbcConfig;
        }
    }
}
