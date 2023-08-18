package com.example.cameraapplication;

import android.net.wifi.hotspot2.pps.Credential;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;

public class RunnerHelper {

   private static final Logger logger = Logger.getLogger(RunnerHelper.class);

/*
    public void runner(CmdObj cmdObj) throws Exception {

        StringBuilder cmdLog = new StringBuilder();
        StringBuilder message = new StringBuilder();
        BufferedReader br = null;
        String cmdOutputLine;

        Process subProcess = Runtime.getRuntime().exec(this.generateMavenCommand(cmdObj));
        br = new BufferedReader(new InputStreamReader(subProcess.getInputStream()));

        while ((cmdOutputLine = br.readLine()) != null) {
            logger.info(System.currentTimeMillis() + "  -  "+ cmdOutputLine);
            //TOOD: If any exception happens, need to tweak them here.
        }

    }

 */

    public void runner(AppCredential credential, CmdObj cmdObj, String localPicPath, String uploadTo, String downloadFrom) throws Exception {

        Session session = null;
        ChannelExec channel = null;

        try {
            session = new JSch().getSession(credential.getUserName(), credential.getIp(), Integer.parseInt(credential.getPort()));
            session.setPassword(credential.getPass());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            while (!sftpChannel.isConnected()) {
                Thread.sleep(100);
            }

            ///Upload the pic to server
            sftpChannel.put(localPicPath, uploadTo);

            //if the uploading is succesfull, run the cmd for the jar
            channel = (ChannelExec) session.openChannel("exec");

            channel.setCommand(this.generateCommand(cmdObj, credential)[2]);
            logger.info("Command To Run : " + this.generateCommand(cmdObj, credential)[2]);
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.connect();

            while (!channel.isConnected()) {
                Thread.sleep(100);
            }

            String responseString = new String(responseStream.toByteArray());

            //See how many rug we got.
            System.out.println(responseString);


            //pull the processed image from server.
            sftpChannel.get(downloadFrom, "jarRunner/src/main/images/img_processed.jpeg");


        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }
    private String[] generateCommand(CmdObj cmdObj, AppCredential credential) throws Exception {

        StringBuilder cmd = new StringBuilder()
                .append("cd").append(" ")
                .append(credential.getProjectPath() + "jars")
                .append("&&")
                .append("java")
                .append(" ")
                .append("-jar")
                .append(" ")
                .append("-Djava.library.path=")
                .append(cmdObj.getOpenCvPath())
                .append(" ")
                .append("-DtrainedModel=")
                .append(cmdObj.getTrainedModel())
                .append(" ")
                .append("-DinputImgPath=")
                .append(cmdObj.getInputImagePath())
                .append(" ")
                .append("-DoutImgPath=")
                .append(cmdObj.getOutputImagePath())
                .append(" ")
                .append(cmdObj.getJarName());

        return new String[]{"cmd.exe", "/c", cmd.toString()};
    }

}