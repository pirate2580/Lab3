package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting language codes to their names.
 */
public class LanguageCodeConverter {

    // TODO Naoroj (?): pick appropriate instance variables to store the data necessary for this class
    // maps code to language name
    private Map<String, String> codeConverterMap = new HashMap<>();

    /**
     * Default constructor which will load the language codes from "language-codes.txt"
     * in the resources folder.
     */
    public LanguageCodeConverter() {
        this("language-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the language code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public LanguageCodeConverter(String filename) {

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            // TODO Naoroj (?): use lines to populate the instance variable
            //           tip: you might find it convenient to create an iterator using lines.iterator()
            Iterator<String> iterator = lines.iterator();
            iterator.next();
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] components = line.split("\t");
                codeConverterMap.put(components[1].trim(), components[0].trim());
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns the name of the language for the given language code.
     * @param code the language code
     * @return the name of the language corresponding to the code
     */
    public String fromLanguageCode(String code) {
        return codeConverterMap.get(code);
    }

    /**
     * Returns the code of the language for the given language name.
     * @param language the name of the language
     * @return the 2-letter code of the language
     */
    public String fromLanguage(String language) {
        for (Map.Entry<String, String> entry : codeConverterMap.entrySet()) {
            if (entry.getValue().equals(language)) {
                return entry.getKey();
            }
        }
        return "no language code found!?";
    }

    /**
     * Returns how many languages are included in this code converter.
     * @return how many languages are included in this code converter.
     */
    public int getNumLanguages() {
        return codeConverterMap.size();
    }

    /**
     * Main function.
     * @param args arguments
     */
    public static void main(String[] args) {
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

        // Abkhazian
        System.out.println(languageCodeConverter.fromLanguageCode("ab"));
        // Chechen
        System.out.println(languageCodeConverter.fromLanguageCode("ce"));
        // ca
        System.out.println(languageCodeConverter.fromLanguage("Catalan, Valencian"));
        // lu
        System.out.println(languageCodeConverter.fromLanguage("Luba-Katanga"));
        // 184
        System.out.println(languageCodeConverter.getNumLanguages());
    }
}
