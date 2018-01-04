package com.adq.jenkins.xmljobtodsl;

import java.io.*;
import java.util.Scanner;

/**
 * Created by alanquintiliano on 19/12/17.
 */
public class IOUtils {

    public String readFromFile(String path) throws IOException {
        File file = new File(path);
        return readFromFile(file);
    }

    public String readFromFile(File file) throws IOException {
        StringBuilder result = new StringBuilder("");
        BufferedReader br = new BufferedReader(new FileReader(file));
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

    public File fileFromResource(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(path).getFile());
    }
}
