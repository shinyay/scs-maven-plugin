package com.oracle.jp.shinyay;

import com.oracle.jp.shinyay.util.SCSFunctions;
import com.oracle.jp.shinyay.util.SCSInfo;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "sample", threadSafe = true, defaultPhase = LifecyclePhase.COMPILE)
public class SampleMojo extends AbstractMojo {

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
        getLog().info("identityDomain: " + identitydomain);
        getLog().info("user: " + user);
        getLog().info("password: " + password );
        getLog().info("container: " + storage);

        getSCSAuthToken(identitydomain, user, password);
        listSCSContainers(scsInfo);
        //createSCSContainer(scsInfo);
        //listSCSContainers(scsInfo);
        showSCSContainer(scsInfo);
        deleteAllObjectsInSCSContainer(scsInfo);
        deleteSCSContainer(scsInfo);
        listSCSContainers(scsInfo);

    }

    private void getSCSAuthToken(String identityDomain, String user, String password) {
        try {
            scsInfo = new SCSInfo.SCSInfoBuilder(identityDomain, user, password).log(getLog()).build();
            scsInfo.setSCSProps(SCSFunctions.getSCSAuthToken(scsInfo));
        } catch (Exception e) {
            getLog().error(e);
        }
    }

    private void listSCSContainers(SCSInfo scsInfo) {
        String result = SCSFunctions.listContainers(scsInfo);
        getLog().info(result);
    }

    private void showSCSContainer(SCSInfo scsInfo) {
        scsInfo.setContainerName(storage);
        String result = SCSFunctions.showContainer(scsInfo);
        getLog().info(result);
    }

    private void createSCSContainer(SCSInfo scsInfo) {
        scsInfo.setContainerName(storage);
        String result = SCSFunctions.createContainer(scsInfo);
        getLog().info(result);
    }

    private void deleteSCSContainer(SCSInfo scsInfo) {
        scsInfo.setContainerName(storage);
        String result = SCSFunctions.deleteContainer(scsInfo);
        getLog().info(result);
    }

    private void deleteObjectInSCSContainer(SCSInfo scsInfo) {
        scsInfo.setContainerName(storage);
        scsInfo.setObjectName(object);

    }

    private void deleteAllObjectsInSCSContainer(SCSInfo scsInfo) {
        scsInfo.setContainerName(storage);
        String result = SCSFunctions.deleteAllObjectsInContainer(scsInfo);
        getLog().info(result);
    }
}
