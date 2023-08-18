package com.example.cameraapplication;

public class Run {

    public static void main(String[] args) {


        CmdObj cmdObj = new CmdObj("/usr/local/share/java/opencv4/",
                "/var/www/rug-counter/models/load_model.onnx",
                "rugcounter.jar",
                "/var/www/rug-counter/inputImages/img_unprocessed.jpeg",
                "/var/www/rug-counter/outputImages/img_processed.jpeg",
                "/usr/local/share/java/opencv4/");


       AppCredential credential = new AppCredential("165.22.3.2",
                "root",
                "GkRcGH6FeWdvyXQW5MDC",
                "https://rug-counter.boutiquerugs.com/",
                "/var/www/rug-counter/",
                "22");
        RunnerHelper runnerHelper = new RunnerHelper();

        try {
            runnerHelper.runner(credential, cmdObj,
                    "jarRunner/src/main/images/img_unprocessed.jpg",
                    "/var/www/rug-counter/inputImages/img_unprocessed.jpeg",
                    "/var/www/rug-counter/outputImages/img_processed.jpeg");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}