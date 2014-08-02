package Constraint2;

import java.io.*;
import java.util.*;

public class Crossword {

    public static String lcs(String a, String b) {

        int[][] lengths = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    lengths[i + 1][j + 1] = lengths[i][j] + 1;
                } else {
                    lengths[i + 1][j + 1] = Math.max(lengths[i + 1][j],
                            lengths[i][j + 1]);
                }
            }
        }

        StringBuffer sb = new StringBuffer();

        for (int x = a.length(), y = b.length();
                x != 0 && y != 0;) {

            if (lengths[x][y] == lengths[x - 1][y]) {
                x--;
            } else if (lengths[x][y] == lengths[x][y - 1]) {
                y--;
            } else {
                assert a.charAt(x - 1) == b.charAt(y - 1);

                sb.append(a.charAt(x - 1));

                x--;
                y--;
            }
        }

        return sb.reverse().toString();

    }

    static ArrayList<String> readDictionary(String fileName)
            throws FileNotFoundException {
        Scanner sc = new Scanner(new FileReader(fileName));
        ArrayList<String> dictionary = new ArrayList<String>();

        while (sc.hasNextLine()) {
            String word = sc.nextLine();
            dictionary.add(word);
        }
        return dictionary;
    }

    static String searchInDictionary(String rack, HashMap<String, String> dictionary, int noOfBlankTiles) {
        String maxWord = "";
        int maxScore = 0;
        String dictionaryWord, prev = "";

        /*int max = Integer.MIN_VALUE;
         for (int i = 0; i < dictionary.size(); i++) {
         dictionaryWord = dictionary.get(i);
         int noOfBlankTiles = 7 - inputString.length();
         char letterArray[] = dictionaryWord.toCharArray();
         Arrays.sort(letterArray);
         String newDictionaryWord = new String(letterArray);

         String matchedString = lcs(inputString, newDictionaryWord);
         if (matchedString.length() == newDictionaryWord.length()
         || matchedString.length() == newDictionaryWord.length() - noOfBlankTiles) {

         score = calculateScore(matchedString);

         }

         if (score > max) {
         maxWord = dictionaryWord;
         max = score;
         prev = dictionaryWord;
         }

         }*/
        
        System.out.println(noOfBlankTiles);
        Iterator it = dictionary.keySet().iterator();
        String mismatchString = null;
        String backupRack = rack;
        while (it.hasNext()) {
            
            int allowedMismatch = noOfBlankTiles;
            String word = it.next().toString();
            String pattern = dictionary.get(word);
            String mismatchLetters = "";
            int position = Integer.parseInt("" + pattern.charAt(0));
            
            rack = backupRack;
            for (int i = 0; i < word.length(); i++) {
                if (i == position) {
                    continue;
                }
                if (!rack.contains("" + word.charAt(i))) {
                    allowedMismatch--;
                    mismatchLetters += word.charAt(i);
                   } else {
                    rack = rack.replace(word.charAt(i),'*');
                     
                }
            }
            
            if (allowedMismatch >= 0) {
                int score = calculateScore(word, mismatchLetters);
                if (score > maxScore) {
                    maxWord = word;
                    maxScore = score;
                    mismatchString = mismatchLetters;
                }
                
            }
        }

        return maxWord+" - " +maxScore;
    }

    static int calculateScore(String word, String mismatchLetters) {
        int sum = 0;
        int score[] = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1,
            1, 1, 1, 4, 4, 8, 4, 10};

        for (int i = 0; i < word.length(); i++) {
            sum += score[word.charAt(i) - 'A'];
        }
        
        for (int i = 0; i < mismatchLetters.length(); i++) {
            sum -= score[mismatchLetters.charAt(i) - 'A'];
        }
        return sum;
    }

    public static void printResult(String input, String result) {

        String[] splitString = result.split(",");
        System.out.println(input + " " + splitString[0] + " " + splitString[1]);

    }

    /*public static String getWordsWithFixedAlpha(String alpha, int length, int position, List<String> dictionary) {
     Iterator<String> iter = dictionary.iterator();
     int maxScore = 0;
     String maxScoreWord = "";

     while (iter.hasNext()) {
     String word = iter.next();

     if (word.indexOf(alpha) >= 0 && word.indexOf(alpha) <= position
     && word.length() <= length) {
     int score = calculateScore(word);

     if (score > maxScore) {
     maxScore = score;
     maxScoreWord = word;
     }
     }
     }

     return maxScoreWord + "," + maxScore;

     }*/
    public static String getAlpha(String input) {
        return input.replaceAll("_", "");
    }

    public static int getAlphaPosition(String alpha, String scrambledWord) {
        return scrambledWord.indexOf(alpha);
    }

    public static ArrayList<String> possiblePatterns(int alphaPosition, int patternLength, String alpha) {
        int leadingSpaces = alphaPosition;
        int trailingSpaces = patternLength - alphaPosition - 1;

        ArrayList<String> patternList = new ArrayList<String>();
        for (int sum = patternLength; sum > 1; sum--) {
            for (int i = 0; i <= leadingSpaces; i++) {
                for (int j = 0; j <= trailingSpaces; j++) {
                    if (i + j + 1 == sum) {
                        patternList.add("" + i + alpha + "-" + sum);
                    }
                }
            }
        }
        return patternList;
    }

    public static String checkPatternMatches(String word, ArrayList<String> patternList) {
        for (String s : patternList) {
            StringTokenizer st = new StringTokenizer(s, "-");
            String temp = st.nextToken();
            int pos = Integer.parseInt("" + temp.charAt(0));
            char alpha = temp.charAt(1);
            int patternLength = Integer.parseInt(st.nextToken());
            int trailingCharacters = word.length() - pos - 1;
            int trailingSpaces = patternLength - pos - 1;
            if (word.length() == patternLength) {
                if (word.charAt(pos) == alpha && trailingCharacters == trailingSpaces) {
                    return s;
                }
            }
        }
        return "";
    }

    public static HashMap<String, String> matchedWordsDictionary(ArrayList<String> dictionary, ArrayList<String> matchedPatterns) {
        HashMap<String, String> matchedWords = new HashMap<String, String>();
        Iterator it = dictionary.iterator();
        while (it.hasNext()) {
            String word = it.next().toString();
            String pattern = checkPatternMatches(word, matchedPatterns);
            if (!pattern.equals("")) {
                matchedWords.put(word, pattern);
            }
        }
        return matchedWords;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String alpha;
        int length;
        int position;

        ArrayList<String> dictionary = new ArrayList<String>();
        ArrayList<String> matchedPatterns;
        HashMap<String, String> matchedWords;
        String fileName = "sowpods.txt";
        dictionary = readDictionary(fileName);
        Scanner sc = new Scanner(new FileReader("testScramble.txt"));

        while (sc.hasNextLine()) {
            String inputLine = sc.nextLine();
            String pattern = inputLine.split(" ")[0];
            String rack = inputLine.split(" ")[1];

            alpha = getAlpha(pattern);
            position = getAlphaPosition(alpha, pattern);
            length = rack.length();
            matchedPatterns = possiblePatterns(position, pattern.length(), alpha);
            System.out.println(matchedPatterns);
            matchedWords = matchedWordsDictionary(dictionary, matchedPatterns);
            //System.out.println(matchedWords);
            String winningWord = searchInDictionary(rack, matchedWords, 7 - length);
            System.out.println(winningWord);
        }

        sc.close();

    }
}
