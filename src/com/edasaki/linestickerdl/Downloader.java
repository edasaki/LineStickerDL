package com.edasaki.linestickerdl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Scanner;

public class Downloader {

    /**
     * Download all files from a given list of URLs. Files are saved to /output/. Files with the phrase "_key" in them are saved to /output_key/.
     * @param urls - a list of URLs to download
     * @throws IOException
     */
    public static void download(List<String> urls) throws IOException {
        System.out.println("Note: download() will automatically separate urls with _key in the to a separate folder.");
        File dir = new File("output" + File.separator);
        File dir_key = new File("output_key" + File.separator);
        if (!dir.exists())
            dir.mkdirs();
        if (!dir_key.exists())
            dir_key.mkdirs();
        for (String s : urls) {
            URL website = new URL(s);

            File file;
            if (s.contains("_key")) {
                file = new File(dir_key.getPath() + File.separator + s.substring(s.lastIndexOf('/') + 1));
            } else {
                file = new File(dir.getPath() + File.separator + s.substring(s.lastIndexOf('/') + 1));
            }
            if (!file.exists())
                file.createNewFile();
            Path target = file.toPath();
            System.out.println("Downloading to " + target + ". Source: " + s);
            try (InputStream in = website.openStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        System.out.println("Download complete.");
    }

    /**
     * Download all stickers between a range of URLs.
     * @deprecated  Tricky user input and unreliable. Alternative of downloading by feeding a https://store.line.me/stickershop/product/ URL into scrape and passing the list to download() is far easier.
     */
    @Deprecated
    public static void downloadRange() throws IOException {
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        System.out.println("starting url? Format MUST follow http://dl.stickershop.line.naver.jp/products/0/0/1/XXXX/android/stickers/XXXX_key.png");
        String start = scan.nextLine().trim();
        System.out.println("ending url?");
        String end = scan.nextLine().trim();
        long startVal = Long.parseLong(start.substring(start.lastIndexOf('/') + 1).replaceAll("[^0-9]", ""));
        long endVal = Long.parseLong(end.substring(end.lastIndexOf('/') + 1).replaceAll("[^0-9]", ""));
        String prefix = start.substring(0, start.lastIndexOf('/') + 1);
        System.out.println("Downloading from " + startVal + " to " + endVal);
        File dir = new File("output" + File.separator);
        if (!dir.exists())
            dir.mkdirs();
        for (long k = startVal; k <= endVal; k++) {
            URL website = new URL(prefix + k + "_key.png");
            File file = new File(dir.getPath() + File.separator + k + "_key.png");
            if (!file.exists())
                file.createNewFile();
            Path target = file.toPath();
            System.out.println("Downloading " + target.toString());
            try (InputStream in = website.openStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
