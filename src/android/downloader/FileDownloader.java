package de.kolbasa.apkupdater.downloader;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Observable;

public abstract class FileDownloader extends Observable {

    private static final String LOCK_MARKER = ".lock";

    public abstract void onProgress(int total, int current);

    private boolean interrupted;

    protected void interrupt() {
        interrupted = true;
    }

    protected void download(String fileUrl, String destination, int timeout) throws IOException {
        download(fileUrl, destination, timeout, null);
    }

    protected void download(String fileUrl, String destination, int timeout, JSONObject body) throws IOException {
        interrupted = false;

        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setUseCaches(false);
        connection.setAllowUserInteraction(false);
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);

        if (body != null) {
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.getOutputStream().write(body.toString().getBytes(StandardCharsets.UTF_8));
            connection.getOutputStream().close();
        }

        connection.connect();

        File file = new File(destination);

        File outputFile = new File(file, fileName);
        File lockFile = new File(file, fileName + LOCK_MARKER);

        // noinspection ResultOfMethodCallIgnored
        outputFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(outputFile);
        InputStream is;
        try {
            is = connection.getInputStream();
        } catch (Exception err) {
            //noinspection ResultOfMethodCallIgnored
            outputFile.delete();
            throw err;
        }
        byte[] buffer = new byte[1024];

        int bytes;
        int bytesDownloaded = 0;
        int fileLength = connection.getContentLength();

        this.onProgress(fileLength, bytesDownloaded);

        //noinspection ResultOfMethodCallIgnored
        lockFile.createNewFile();

        while ((bytes = is.read(buffer)) != -1 && !interrupted) {
            bytesDownloaded += bytes;
            fos.write(buffer, 0, bytes);
            this.onProgress(fileLength, bytesDownloaded);
        }

        fos.flush();
        fos.close();
        is.close();
        connection.disconnect();

        //noinspection ResultOfMethodCallIgnored
        lockFile.delete();
    }

}
