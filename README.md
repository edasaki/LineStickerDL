# LineStickerDL
Tool for downloading LINE stickers and building XML files for Xenforo smiley importing from image directories.

## Disclaimer
This was mostly coded to practice implementing certain features. All LINE stickers are copyrighted by their respective copyright owners, and actually using these images for your own purposes likely constitutes copyright infringement. Please be aware of relevant copyright law if you choose to use this!

## Dependencies
None. Compile with any Java compiler and you're good to go!

## Example Usage
Example of how I would upload [these stickers](https://store.line.me/stickershop/product/5271/en) onto my Xenforo forums to use as smileys.

1. Use option 1: Full sticker download
2. Type the URL and press enter: https://store.line.me/stickershop/product/5271/en
3. Stickers are now downloaded to /output/ and /output_key/ folders in the source directory
4. Keep the folder that you want. /output/ contains large images and /output_key/ contains smaller images (_key is more suitable for forums in my opinion)
5. Upload folder to your forums.
6. Use option 2: Generate Xenforo XML
7. Type in the local directory (go into your output folder and copy the address)
8. Type in the web directory (where you uploaded the images). The base folder is your forums root. So for example if you uploaded to the Xenforo styles folder, you would type /styles/blah/blah
9. Type in the set name. This is the name of the category of smilies, and can easily be changed later.
10. Type in the individual smiley name. This has to be changed one at a time later, so try to choose a name that you can use forever. When users hover over the sticker in your emoji selector, they will see this name.
11. Type in the set ID. This is the ID of the category in Xenforo, make sure it's unique!
12. Type in the set display order. This can be easily changed later.
13. Type in the set replace text. This has to be changed one at a time later, so try to choose a replace text that is unique and easy to use. This is what users will use to input your smileys if they want to manually type them out.
14. The XML file will be created in your source directory as output.xml.
15. Head to Import Smileys in Xenforo admin panel and import using that XML file.
16. All done! Enjoy your new smileys.
