package org.ruoqing.config;

import lombok.Data;

@Data
public class EntityConfig {

    private String tableName;

    private boolean lombok;

    public static class Builder {
        private final EntityConfig entityConfig;

        public Builder() {
            this.entityConfig = new EntityConfig();
        }

        public Builder(EntityConfig entityConfig) {
            this.entityConfig = entityConfig;
        }

        public Builder tableName(String tableName) {
            this.entityConfig.tableName = tableName;
            return this;
        }

        public Builder enableLombok() {
            this.entityConfig.lombok = true;
            return this;
        }

        public EntityConfig build() {
            return this.entityConfig;
        }

    }


}
