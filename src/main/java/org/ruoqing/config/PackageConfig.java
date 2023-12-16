package org.ruoqing.config;

import lombok.Data;

import java.io.File;
import java.nio.file.Paths;

@Data
public class PackageConfig {

    private static final String projectPath = System.getProperty("user.dir");

    private static final String SEPARATOR = File.separator;

    private String path;

    private String parentPackage;

    private String parentPath;

    public static class Builder {
        private final PackageConfig packageConfig;

        public Builder() {
            this.packageConfig = new PackageConfig();
            this.packageConfig.path = Paths.get(projectPath, "src", "main", "java") + SEPARATOR;
        }

        public Builder(PackageConfig packageConfig) {
            this.packageConfig = packageConfig;
        }

        public Builder parent(String parent) {
            this.packageConfig.parentPackage = parent;
            this.packageConfig.parentPath = parent.replace(".", SEPARATOR) + SEPARATOR;
            return this;
        }

        public Builder path(String path) {
            this.packageConfig.path = path;
            return this;
        }

        public PackageConfig build() {
            return this.packageConfig;
        }

    }

}
