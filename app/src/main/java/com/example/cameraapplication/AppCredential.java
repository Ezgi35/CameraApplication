package com.example.cameraapplication;
public class AppCredential {

    private String ip;
    private String userName;
    private String pass;
    private String url;
    private String projectPath;

    private String port;

    public AppCredential(String ip, String userName, String pass, String url, String projectPath, String port) {
        this.ip = ip;
        this.userName = userName;
        this.pass = pass;
        this.url = url;
        this.projectPath = projectPath;
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
}