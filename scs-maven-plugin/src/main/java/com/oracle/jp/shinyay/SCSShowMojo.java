package com.oracle.jp.shinyay;

import com.oracle.jp.shinyay.util.SCSFunctions;
import com.oracle.jp.shinyay.util.SCSInfo;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "show", threadSafe = true, defaultPhase = LifecyclePhase.INSTALL)
public class SCSShowMojo extends AbstractMojo {

    @Parameter(required = true)
    private String identitydomain;

    @Parameter(required = true)
    private String user;

    @Parameter(required = true)
    private String password;

    @Parameter(required = true)
    private String storage;

    @Parameter()
    private String object;

    private static SCSInfo scsInfo;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getSCSAuthToken(identitydomain, user, password);
        showSCSContainer();
    }

    private void getSCSAuthToken(String identityDomain, String user, String password) {
        try {
            scsInfo = new SCSInfo.SCSInfoBuilder(identityDomain, user, password)
                    .containerName(storage)
                    .log(getLog()).build();
            scsInfo.setScsProps(SCSFunctions.getSCSAuthToken(scsInfo));
        } catch (Exception e) {
            getLog().error(e);
        }
    }

    private void showSCSContainer() {
        try {
            String result = SCSFunctions.showContainer(scsInfo);
            getLog().info(result);
        } catch (Exception e) {
            getLog().error(e);
        }
    }
}
