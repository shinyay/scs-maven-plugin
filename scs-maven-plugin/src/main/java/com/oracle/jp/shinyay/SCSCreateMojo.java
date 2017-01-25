package com.oracle.jp.shinyay;

import com.oracle.jp.shinyay.util.SCSFunctions;
import com.oracle.jp.shinyay.util.SCSInfo;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "create", threadSafe = true, defaultPhase = LifecyclePhase.INSTALL)
public class SCSCreateMojo extends AbstractMojo {

    @Parameter(required = true)
    private  String identitydomain;

    @Parameter(required = true)
    private  String user;

    @Parameter(required = true)
    private String password;

    @Parameter(required = true)
    private String storage;

    @Parameter()
    private String object;

    private static SCSInfo scsInfo;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("IDENTITY DOMAIN: " + identitydomain);
        getLog().info("CLOUD USER: " + user);
        getLog().info("PASSWORD: " + password );
        getLog().info("SCS CONTAINER: " + storage);

        getSCSAuthToken(identitydomain, user, password);
        createSCSContainer(scsInfo);
    }

    private void getSCSAuthToken(String identityDomain, String user, String password) {
        try {
            scsInfo = new SCSInfo.SCSInfoBuilder(identityDomain, user, password).log(getLog()).build();
            scsInfo.setSCSProps(SCSFunctions.getSCSAuthToken(scsInfo));
        } catch (Exception e) {
            getLog().error(e);
        }
    }

    private void createSCSContainer(SCSInfo scsInfo) {
        scsInfo.setContainerName(storage);
        String result = SCSFunctions.createContainer(scsInfo);
        getLog().info(result);
    }
}
