package com.oracle.jp.shinyay.util;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.util.Properties;

public class SCSInfo {

    private final String username;
    private final String password;
    private final String identityDomain;

    private final Log log;

    private Properties scsProps;
    private String containerName;
    private String objectName;

    public static class SCSInfoBuilder {
        private final String username;
        private final String password;
        private final String identityDomain;

        private Log log;

        public SCSInfoBuilder(String identityDomain, String username, String password) {
            this.identityDomain = identityDomain;
            this.username = username;
            this.password = password;
            this.log = new SystemStreamLog();
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
        username = builder.username;
        password = builder.password;
        identityDomain = builder.identityDomain;
        log = builder.log;
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

    public void setSCSProps(Properties scsProps) {
        this.scsProps = scsProps;
    }

    public Properties getSCSProps() {
        return scsProps;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getContainerName() {
        return containerName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
