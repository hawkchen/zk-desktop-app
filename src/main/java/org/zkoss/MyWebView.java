package org.zkoss;

import javafx.application.Application;
import javafx.beans.value.*;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.*;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.Base64;
import java.util.concurrent.*;
import java.util.function.Consumer;


/**
 * https://docs.oracle.com/javase/8/javafx/get-started-tutorial/hello_world.htm
 */
public class MyWebView extends Application {
    final String PNG_DATA_URL_PREFIX = "data:image/png;base64,";

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
        webView.setContextMenuEnabled(true);
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

        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        if (newValue != Worker.State.SUCCEEDED) {
                            return;
                        }

                        JSObject window = (JSObject) webEngine.executeScript("window");
                        window.setMember("imageSaver", new ImageSaver());
                    }
                }
        );

        webEngine.load("http://localhost:8080");
        StackPane root = new StackPane();
        root.setStyle("width:100%;" + "height:100%;");
        root.getChildren().add(webView);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setScene(scene);
//        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private boolean isPngImage(String newLoc) {
        return newLoc.startsWith(PNG_DATA_URL_PREFIX);
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }
}