package com.edasaki.linestickerdl;

import java.io.IOException;
import java.util.Scanner;

/**
 * https://github.com/edasaki/LineStickerDL/
 * @author Edasaki
 */

public class LineStickerDL {

    public static void main(String args[]) throws IOException, InterruptedException {
        while (true) {
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("LineStickerDL by Edasaki");
            System.out.println("Large and small refer to the sticker image size.");
            System.out.println("1. Full sticker download.");
            System.out.println("2. Generate Xenforo Smiley XML.");
            System.out.println("3. Sticker download - Large images only.");
            System.out.println("4. Sticker download - Small images only.");
            System.out.println("5. Only scrape URLs, NO download.");
            System.out.println("6. Only scrape large image URLs, NO download.");
            System.out.println("7. Only scrape small image URLs, NO download.");
            System.out.println("8. URL download. DEPRECATED - NOT RECOMMENDED FOR USE.");
            System.out.println("9. Exit.");
            System.out.println("");
            System.out.println("Please enter your choice:");
            Scanner scan = new Scanner(System.in);
            int choice = Integer.parseInt(scan.nextLine().trim());
            switch (choice) {
                case 1:
                    Downloader.download(Scraper.scrapeURL(true, true));
                    break;
                case 2:
                    XenforoXML.writeXML();
                    break;
                case 3:
                    Downloader.download(Scraper.scrapeURL(true, false));
                    break;
                case 4:
                    Downloader.download(Scraper.scrapeURL(false, true));
                    break;
                case 5:
                    for (String s : Scraper.scrapeURL(true, true))
                        System.out.println(s);
                    break;
                case 6:
                    for (String s : Scraper.scrapeURL(true, false))
                        System.out.println(s);
                    break;
                case 7:
                    for (String s : Scraper.scrapeURL(false, true))
                        System.out.println(s);
                    break;
                case 8:
                    Downloader.downloadRange();
                    break;
                case 9:
                    System.out.println("Closing LineStickerDL...");
                    Thread.sleep(3000);
                    scan.close();
                    System.exit(0);
                    break;
            }
        }
    }

}
