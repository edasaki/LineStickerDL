package com.edasaki.linestickerdl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class XenforoXML {

    private static final String[] IMAGE_EXTENSIONS = { ".png", ".jpg", ".jpeg", ".gif" };
    
    /**
     * Creates an XML file that can be used to import smileys in Xenforo. Requires a lot of user input from System.in to setup the XML.
     * @throws IOException
     */
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
}
