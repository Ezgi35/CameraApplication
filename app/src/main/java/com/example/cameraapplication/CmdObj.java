package com.example.cameraapplication;

import java.util.Objects;

public class CmdObj {

    private String jarPath;
    private String trainedModel;
    private String jarName;
    private String inputImagePath;
    private String outputImagePath;
    private String openCvPath;

    public CmdObj(String jarPath, String trainedModel, String jarName, String inputImagePath, String outputImagePath, String openCvPath) {
        this.jarPath = jarPath;
        this.trainedModel = trainedModel;
        this.jarName = jarName;
        this.inputImagePath = inputImagePath;
        this.outputImagePath = outputImagePath;
        this.openCvPath = openCvPath;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public String getTrainedModel() {
        return trainedModel;
    }

    public void setTrainedModel(String trainedModel) {
        this.trainedModel = trainedModel;
    }

    public String getJarName() {
        return jarName;
    }

    public void setJarName(String jarName) {
        this.jarName = jarName;
    }

    public String getInputImagePath() {
        return inputImagePath;
    }

    public void setInputImagePath(String inputImagePath) {
        this.inputImagePath = inputImagePath;
    }

    public String getOutputImagePath() {
        return outputImagePath;
    }

    public void setOutputImagePath(String outputImagePath) {
        this.outputImagePath = outputImagePath;
    }

    public String getOpenCvPath() {
        return openCvPath;
    }

    public void setOpenCvPath(String openCvPath) {
        this.openCvPath = openCvPath;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;


        /*if (!(o instanceof CmdObj cmdObj)) return false;
        return Objects.equals(getJarPath(), cmdObj.getJarPath()) && Objects.equals(getTrainedModel(), cmdObj.getTrainedModel()) && Objects.equals(getJarName(), cmdObj.getJarName()) && Objects.equals(getInputImagePath(), cmdObj.getInputImagePath()) && Objects.equals(getOutputImagePath(), cmdObj.getOutputImagePath()) && Objects.equals(getOpenCvPath(), cmdObj.getOpenCvPath());
    */
        return false;
    }


    @Override
    public int hashCode() {
        return Objects.hash(getJarPath(), getTrainedModel(), getJarName(), getInputImagePath(), getOutputImagePath(), getOpenCvPath());
    }

    @Override
    public String toString() {
        return "model.CmdObj{" +
                "jarPath='" + jarPath + '\'' +
                ", trainedModel='" + trainedModel + '\'' +
                ", jarName='" + jarName + '\'' +
                ", inputImagePath='" + inputImagePath + '\'' +
                ", outputImagePath='" + outputImagePath + '\'' +
                ", openCvPath='" + openCvPath + '\'' +
                '}';
    }
}