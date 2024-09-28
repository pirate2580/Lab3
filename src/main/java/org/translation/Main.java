package org.translation;

// import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
        Translator translator = new JSONTranslator();
        // Translator translator = new InLabByHandTranslator();

        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        while (true) {
            String quitter = "quit";
            String country = promptForCountry(translator);
            if (country.equals(quitter)) {
                break;
            }

            CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
            String countryCode = countryCodeConverter.fromCountry(country).toLowerCase();

            // note: countryCode is in upper case because of country-codes.txt but needs to be in lowercase for internal
            // translator which uses 3-letter lowercase
            String language = promptForLanguage(translator, countryCode);
            if (language.equals(quitter)) {
                break;
            }

            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
            String languageCode = languageCodeConverter.fromLanguage(language);
            // System.out.println("test " + countryCode + " " + languageCode);
            System.out.println(country + " in " + language + " is " + translator.translate(countryCode, languageCode));
            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if ("quit".equals(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        // list of 3-letter country codes
        List<String> countryCodes = translator.getCountries();

        // these should be country names, unsorted
        List<String> countryNames = new ArrayList<String>();
        // countrycode converter class
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        for (int i = 0; i < countryCodes.size(); i++) {
            // convert country code to country name
            // System.out.println("testing" + countryCodeConverter.fromCountryCode(countryCodes.get(i).toUpperCase()));
            countryNames.add(countryCodeConverter.fromCountryCode(countryCodes.get(i).toUpperCase()));
        }
        // sort country names alphabetically
        Collections.sort(countryNames);
        for (int i = 0; i < countryNames.size(); i++) {
            System.out.println(countryNames.get(i));
        }

        System.out.println("select a country from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String country) {

        // country
        List<String> languageCodes = translator.getCountryLanguages(country);

        List<String> languageNames = new ArrayList<>();
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

        for (int i = 0; i < languageCodes.size(); i++) {
            languageNames.add(languageCodeConverter.fromLanguageCode(languageCodes.get(i)));
        }
        Collections.sort(languageNames);
        for (int i = 0; i < languageNames.size(); i++) {
            System.out.println(languageNames.get(i));
        }

        System.out.println("select a language from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
