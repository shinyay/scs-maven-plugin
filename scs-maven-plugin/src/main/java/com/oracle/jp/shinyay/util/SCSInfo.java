package com.oracle.jp.shinyay.util;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.util.Properties;

public class SCSInfo {

    private final String username;
    private final String password;
    private final String identityDomain;


    private final String containerName;
    private final String objectName;
    private final String targetPath;

    private Properties scsProps;
    private final Log log;

    public static class SCSInfoBuilder {
        private final String username;
        private final String password;
        private final String identityDomain;

        private Properties scsProps;
        private String containerName;
        private String objectName;
        private String targetPath;

        private Log log;

        public SCSInfoBuilder(String identityDomain, String username, String password) {
            this.identityDomain = identityDomain;
            this.username = username;
            this.password = password;
            this.log = new SystemStreamLog();
        }

        public SCSInfoBuilder scsProps(Properties scsProps){
            this.scsProps = scsProps;
            return this;
        }

        public SCSInfoBuilder containerName(String containerName){
            this.containerName = containerName;
            return this;
        }

        public SCSInfoBuilder objectName(String objectName){
                this.objectName=objectName;
                return this;
        }

        public SCSInfoBuilder targetPath(String targetPath){
            this.targetPath = targetPath;
            return this;
        }

        public SCSInfoBuilder log(Log log) {
            this.log = log;
            return this;
        }

        public SCSInfo build() {
            return new SCSInfo(this);
        }
    }

    private SCSInfo(SCSInfoBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.identityDomain = builder.identityDomain;
        this.scsProps = builder.scsProps;
        this.containerName = builder.containerName;
        this.objectName = builder.objectName;
        this.targetPath = builder.targetPath;
        this.log = builder.log;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIdentityDomain() {
        return identityDomain;
    }

    public Log getLog() {
        return log;
    }

    public Properties getSCSProps() {
        return scsProps;
    }

    public void setScsProps(Properties scsProps) {
        this.scsProps = scsProps;
    }

    public String getContainerName() {
        return containerName;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getTargetPath() {
        return targetPath;
    }

}
