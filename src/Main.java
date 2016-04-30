import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

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
                    download(scrape(true, true));
                    break;
                case 2:
                    writeXML();
                    break;
                case 3:
                    download(scrape(true, false));
                    break;
                case 4:
                    download(scrape(false, true));
                    break;
                case 5:
                    for (String s : scrape(true, true))
                        System.out.println(s);
                    break;
                case 6:
                    for (String s : scrape(true, false))
                        System.out.println(s);
                    break;
                case 7:
                    for (String s : scrape(false, true))
                        System.out.println(s);
                    break;
                case 8:
                    downloadRange();
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

    private static final String PRE_URL = "background-image:url(";
    private static final String POST_URL = "); background-size";

    public static List<String> scrape(boolean includeLarge, boolean includeSmall) {
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        System.out.println("Warning: This function relies on manual parsing of the LINE website, and may break at any time if their website changes! Check https://github.com/edasaki/LineStickerDL for updates if this breaks.");
        System.out.println("Please enter the LINE Store URL. Example URL: https://store.line.me/stickershop/product/2462/en");
        String url = scan.nextLine().trim();
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

    private static final String[] IMAGE_EXTENSIONS = { ".png", ".jpg", ".jpeg", ".gif" };

    public static void writeXML() throws IOException {
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        System.out.println("Local smiley directory? (smiley folder on your computer)");
        String localdir = scan.nextLine().trim();
        System.out.println("Directory on website? (where you uploaded it) Example: emojis/puppers");
        String webdir = scan.nextLine().trim();
        System.out.println("Smiley set name? (category name that displays in emoji selector)");
        String setname = scan.nextLine().trim();
        System.out.println("Smiley individual name? (individual name of emojis) Example: if you name \"Pup\", then stickers will be called Pup 1, Pup 2, etc.");
        String setindividual = scan.nextLine().trim();
        System.out.println("Smiley set ID? (ID for the category on Xenforo - make sure it doesn't overlap an existing category)");
        String setid = scan.nextLine().trim();
        System.out.println("Smiley set display order?");
        String setdisplayorder = scan.nextLine().trim();
        System.out.println("Smiley set replace text? the text that you can type the smiley with. For example, if you do \"doober\", your smileys will be typed with :doober1:, :doober2:, :doober3:, etc.");
        String setreplace = scan.nextLine().trim();
        if (!webdir.endsWith("/"))
            webdir += "/";
        File f = new File(localdir);
        if (!f.exists() || !f.isDirectory()) {
            System.err.println("Could not find directory " + localdir);
            return;
        }
        int count = 0;
        ArrayList<File> images = new ArrayList<File>();
        for (File img : f.listFiles()) {
            for (String s : IMAGE_EXTENSIONS) {
                if (img.getName().endsWith(s)) {
                    images.add(img);
                    count++;
                    break;
                }
            }
        }
        System.out.println("Found " + count + " smileys.");
        System.out.println("Writing XML...");
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File("output.xml"))));
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<smilies_export>");
        out.println("  <smilie_categories>");
        out.println("    <smilie_category id=\"" + setid + "\" title=\"" + setname + "\" display_order=\"" + setdisplayorder + "\"/>");
        out.println("  </smilie_categories>");
        out.println("  <smilies>");
        for (int k = 0; k < images.size(); k++) {
            File img = images.get(k);
            out.println("    <smilie smilie_category_id=\"" + setid + "\" title=\"" + setindividual + " " + (k + 1) + "\" display_order=\"" + (k + 1) + "\" display_in_editor=\"1\">");
            out.println("      <image_url>" + webdir + img.getName() + "</image_url>");
            out.println("      <smilie_text>:" + setreplace + (k + 1) + ":</smilie_text>");
            out.println("    </smilie>");
        }
        out.println("  </smilies>");
        out.println("</smilies_export>");
        out.close();
        System.out.println("XML write complete.");
    }

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
