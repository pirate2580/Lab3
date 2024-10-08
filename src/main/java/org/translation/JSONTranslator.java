package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // maps the 3 letter country code to its index in sample.json
    private final Map<String, Integer> countryToIndex = new HashMap<>();
    // maps index in sample.json to the list of languages for that 3-letter country code
    private final Map<Integer, List<String>> countryLanguages = new HashMap<>();
    // maps index in sample.json to the list of translations for that 3-letter country code
    private Map<Integer, List<String>> countryTranslations = new HashMap<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String country = jsonObject.getString("alpha3");
                countryToIndex.put(country, i);

                List<String> codes = new ArrayList<>();
                List<String> translated = new ArrayList<>();
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if ("id".equals(key) || "alpha2".equals(key) || "alpha3".equals(key)) {
                        continue;
                    }
                    // translation of
                    Object value = jsonObject.get(key);
                    codes.add(key);
                    translated.add((String) value);
                }
                countryLanguages.put(i, codes);
                countryTranslations.put(i, translated);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {

        List<String> languageCodes = countryLanguages.get(countryToIndex.get(country));
        return new ArrayList<String>(languageCodes);
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(countryToIndex.keySet());
    }

    @Override
    public String translate(String country, String language) {
        int num = countryToIndex.get(country);
        int idx = countryLanguages.get(num).indexOf(language);
        return countryTranslations.get(num).get(idx);
    }
}
