package com.edasaki.linestickerdl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Scraper {

    private static final String PRE_URL = "background-image:url(";
    private static final String POST_URL = "); background-size";

    /**
     * Returns a list of direct URLs to images in a LINE sticker pack. Requires user input from System.in for URL.
     * @param includeLarge - Include URLs for large versions of stickers
     * @param includeSmall - Include URLs for small ("key") versions of stickers
     * @return a list of direct URLs to images in a LINE sticker pack
     */
    public static List<String> scrapeURL(boolean includeLarge, boolean includeSmall) {
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        System.out.println("Warning: This function relies on manual parsing of the LINE website, and may break at any time if their website changes! Check https://github.com/edasaki/LineStickerDL for updates if this breaks.");
        System.out.println("Please enter the LINE Store URL. Example URL: https://store.line.me/stickershop/product/2462/en");
        String url = scan.nextLine().trim();
        return scrapeURL(url, includeLarge, includeSmall);
    }

    /**
     * Returns a list of direct URLs to images in a LINE sticker pack. 
     * @param url - URL of the LINE sticker pack
     * @param includeLarge - Include URLs for large versions of stickers
     * @param includeSmall - Include URLs for small ("key") versions of stickers
     * @return a list of direct URLs to images in a LINE sticker pack
     */
    public static List<String> scrapeURL(String url, boolean includeLarge, boolean includeSmall) {
        ArrayList<String> urls = new ArrayList<String>();
        try {
            System.out.println("Reading URL " + url + ". Establishing connection...");
            URL page = new URL(url);
            // Read in the URL
            BufferedReader in = new BufferedReader(new InputStreamReader(page.openStream(), "UTF-8"));
            String s;
            while ((s = in.readLine()) != null) {
                s = s.trim();
                if (s.contains("background-image:url(")) {
                    s = s.substring(s.indexOf(PRE_URL) + PRE_URL.length());
                    s = s.substring(0, s.indexOf(POST_URL));
                    if (includeLarge)
                        urls.add(s);
                    String small = new String(s);
                    String temp = small.substring(0, small.lastIndexOf('.'));
                    temp += "_key";
                    temp += small.substring(small.lastIndexOf('.'));
                    if (includeSmall)
                        urls.add(temp);
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String s : urls)
            System.out.println(s);
        return urls;
    }
}
