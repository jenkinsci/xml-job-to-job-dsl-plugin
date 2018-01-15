package com.adq.jenkins.xmljobtodsl.utils;

import java.io.*;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by alanquintiliano on 19/12/17.
 */
public class IOUtils {

    public String readFromFile(String path) throws IOException {
        File file = new File(path);
        return readFromFile(file);
    }

    public String readFromFile(File file) throws IOException {
        return readFromStream(new FileReader(file));
    }

    private String readFromStream(InputStreamReader stream) throws IOException {
        StringBuilder result = new StringBuilder("");
        BufferedReader br = new BufferedReader(stream);
        try {
            String line = br.readLine();

            while (line != null) {
                result.append(line);
                result.append(System.lineSeparator());
                line = br.readLine();
            }
        } finally {
            br.close();
        }

        return result.toString();
    }

    public String readFromResource(String path) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        return readFromFile(new File(classLoader.getResource(path).getFile()));
    }

    public String readFromUrl(String urlString, final String username, final String password) throws IOException {
        if (username != null && password != null) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password.toCharArray());
                }
            });
        }
        URL url = new URL(urlString);
        URLConnection uc = url.openConnection();
        return readFromStream(new InputStreamReader(uc.getInputStream()));
    }

    public void saveToFile(String text, String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter out = new PrintWriter(file);
        out.println(text);
        out.close();
    }
}
