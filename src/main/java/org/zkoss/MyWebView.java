package org.zkoss;

import javafx.application.Application;
import javafx.beans.value.*;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.concurrent.*;


/**
 * https://docs.oracle.com/javase/8/javafx/get-started-tutorial/hello_world.htm
 */
public class MyWebView extends Application {

    public static void main(String[] args) throws InterruptedException {
        startWebApp();
        while (!started) {
            Thread.sleep(300);
        }
        launch(args);
    }

    private static void startWebApp() {
        ExecutorService exec = Executors.newFixedThreadPool(1);
        Runnable webAppThread = new Runnable() {
            @Override
            public void run() {
                MyWebApp.main(new String[0]);
            }
        };
        exec.execute(webAppThread);
    }

    private static boolean started = false;

    public static void notifyStart() {
        started = true;
    }


    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();

        // Create the WebEngine
        final WebEngine webEngine = webView.getEngine();
        System.out.println(webView.getEngine().getUserAgent());
        // Load the Start-Page
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    //stage.setTitle(webEngine.getLocation());
                    primaryStage.setTitle(webEngine.getTitle());
                }
            }
        });

        webEngine.locationProperty().addListener((observableValue, oldLoc, newLoc) -> {
            if (isImage(newLoc)) {
                try (BufferedInputStream is = new BufferedInputStream(new URL(newLoc).openStream());
                     BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("qrcode.png"));) {
                    bufferedOutputStream.write(is.readAllBytes());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        webEngine.load("http://localhost:8080");
        StackPane root = new StackPane();
        root.setStyle("width:100%;" + "height:100%;");
        root.getChildren().add(webView);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private boolean isImage(String newLoc) {
        return newLoc.startsWith("data:image/octet-stream");
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }
}