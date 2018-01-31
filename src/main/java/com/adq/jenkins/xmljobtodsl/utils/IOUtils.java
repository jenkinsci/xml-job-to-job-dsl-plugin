package com.adq.jenkins.xmljobtodsl.utils;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alanquintiliano on 19/12/17.
 */
public class IOUtils {

    public String readFromFile(String path) throws IOException {
        File file = new File(path);
        return readFromFile(file);
    }

    public String readFromFile(File file) throws IOException {
        return readFromStream(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
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
            stream.close();
        }

        return result.toString();
    }

    public String readFromResource(String path) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        String file = URLDecoder.decode(classLoader.getResource(path).getFile(), StandardCharsets.UTF_8.displayName());
        return readFromFile(new File(file));
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
        return readFromStream(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
    }

    public void saveToFile(String text, String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            boolean createdNewFile = file.createNewFile();
            if (!createdNewFile) {
                throw new IOException("Couldn't create file at path: " + path);
            }
        }
        PrintWriter out = new PrintWriter(file, StandardCharsets.UTF_8.displayName());
        out.println(text);
        out.close();
    }

    public File[] getJobXmlFilesInDirectory(String directory) {
        File dir = new File(directory);
        if (!dir.isDirectory()) {
            throw new RuntimeException(directory + " is not a directory");
        }
        File[] directories = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        if (directories == null) {
            return null;
        }

        List<File> files = new ArrayList<>();
        for (File innerDirectory : directories) {
            File[] innerFiles = innerDirectory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getPath().endsWith("config.xml");
                }
            });

            if (innerFiles == null) {
                continue;
            }

            files.addAll(Arrays.asList(innerFiles));
        }

        return files.toArray(new File[files.size()]);
    }
}
