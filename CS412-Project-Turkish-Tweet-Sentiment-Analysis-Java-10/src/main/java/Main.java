import org.antlr.v4.runtime.Token;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.analysis.tr.TurkishMorphology;
import zemberek.normalization.TurkishSpellChecker;
import zemberek.tokenization.TurkishTokenizer;
import zemberek.tokenization.antlr.TurkishLexer;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class Main {
    private static boolean isStringUpperCase(String stringToCheck) {
        for (int iii = 0; iii < stringToCheck.length(); iii++) {
            if (Character.isLowerCase(stringToCheck.charAt(iii))) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        // Build a Hash Map of stop words for later use
        HashMap<String, Boolean> stopWordsDictionary = new HashMap<String, Boolean>();

        String stopWordsFileName = "/home/ugur/Downloads/CS412 Project/stop_words.txt";

        try {
            FileReader stopWordsFileReader = new FileReader(stopWordsFileName);

            BufferedReader bufferedReader = new BufferedReader(stopWordsFileReader);

            String line;

            while((line = bufferedReader.readLine()) != null) {
                stopWordsDictionary.put(line, Boolean.TRUE);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not find file '" + stopWordsFileName + "' !!!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading file '" + stopWordsFileName + "' !!!");
            e.printStackTrace();
        }

        // Create a TurkishMorphology class for later use
        TurkishMorphology morphology = null;

        try {
            morphology = TurkishMorphology.createWithDefaults();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create TurkishSpellChecker for later use
        TurkishSpellChecker spellChecker = null;

        try {
            spellChecker = new TurkishSpellChecker(morphology);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Opening File to Write Pre-Processed Data
        String outputFileName = "/home/ugur/Downloads/CS412 Project/train_tweets_preprocessed.txt";

        try {
            FileWriter fileWriter = new FileWriter(outputFileName);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Reading Training File for Pre-Processing
            String inputFileName = "/home/ugur/Downloads/CS412 Project/train_tweets_deasciified.txt";

            String line;

            try {
                FileReader fileReader = new FileReader(inputFileName);

                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null) {
                    // Tokenizer
                    TurkishTokenizer tokenizer = TurkishTokenizer.DEFAULT;
                    // Tokenize line
                    List<Token> tokens = tokenizer.tokenize(line.toLowerCase());

                    String newLine = "";

                    for (Token token : tokens) {
                        String tokenText = token.getText().toLowerCase();
                        String tokenType = TurkishLexer.VOCABULARY.getDisplayName(token.getType());

                        // If stop word, ignore the token
                        if (stopWordsDictionary.containsKey(tokenText)) {
                            continue;
                        }
                        // If word is all uppercase, record it
                        if (isStringUpperCase(tokenText) && tokenType.equals("Word")) {
                            System.out.println(tokenText);
                            newLine = newLine.concat("AllUpperCase");
                            newLine = newLine.concat(" ");
                        }

                        // If Emoticon
                        if (tokenType.equals("Emoticon")) {
                            if (tokenText.equals(":-)") || tokenText.equals(":)") || tokenText.equals("=)") || tokenText.equals(":D")) {
                                newLine = newLine.concat("PositiveSmiley");
                                newLine = newLine.concat(" ");
                            } else if (tokenText.equals(":-(") || tokenText.equals(":(") || tokenText.equals("=(") || tokenText.equals(";(")) {
                                newLine = newLine.concat("NegativeSmiley");
                                newLine = newLine.concat(" ");
                            }
                            continue;
                        } else if (tokenText.equals("!")) {
                            newLine = newLine.concat("ExclamationMark");
                            newLine = newLine.concat(" ");
                            continue;
                        } else if (tokenText.equals("?")) {
                            newLine = newLine.concat("QuestionMark");
                            newLine = newLine.concat(" ");
                            continue;
                        } else if (tokenText.equals("http")) {
                            continue;
                        } else if (tokenType.equals("Punctuation")) {
                            continue;
                        }

                        if (!spellChecker.check(tokenText)) {
                            List<String> suggestions = spellChecker.suggestForWord(tokenText);

                            if (suggestions.size() > 0) {
                                tokenText = suggestions.get(0).toLowerCase();
                            }
                        }

                        // Morphology (Stemming and Lemmatization)
                        List<WordAnalysis> results = null;

                        if (morphology != null) {
                            results = morphology.analyze(tokenText);
                        }

                        WordAnalysis firstResult = results.get(0);

                        if (!firstResult.getLemmas().get(0).equals("UNK")) {
                            if (firstResult.getLemma().length() >= 6) {
                                newLine = newLine.concat(firstResult.getLemma().substring(0, 6));
                            } else {
                                newLine = newLine.concat(firstResult.getLemma());
                            }
                            newLine = newLine.concat(" ");
                        }
                    }
                    bufferedWriter.write(newLine.toLowerCase());
                    bufferedWriter.newLine();
                }

                bufferedReader.close();
            }
            catch(FileNotFoundException e) {
                System.out.println("Could not find file '" + inputFileName + "' !!!");
                e.printStackTrace();
            }
            catch(IOException e) {
                System.out.println("Error reading file '" + inputFileName + "' !!!");
                e.printStackTrace();
            }
            bufferedWriter.close();

            System.out.println("Pre-processed training data is written to file: ".concat(outputFileName).concat("!"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
