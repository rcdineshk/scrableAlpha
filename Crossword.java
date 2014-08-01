package team2Scrabble;

import java.io.*;
import java.util.*;

public class Crossword {

	public static String lcs(String a, String b) {

		int[][] lengths = new int[a.length() + 1][b.length() + 1];

		for (int i = 0; i < a.length(); i++)
			for (int j = 0; j < b.length(); j++)
				if (a.charAt(i) == b.charAt(j))
					lengths[i + 1][j + 1] = lengths[i][j] + 1;
				else
					lengths[i + 1][j + 1] = Math.max(lengths[i + 1][j],
							lengths[i][j + 1]);

		StringBuffer sb = new StringBuffer();

		for (int x = a.length(), y = b.length();

		x != 0 && y != 0;) {

			if (lengths[x][y] == lengths[x - 1][y])

				x--;
			else if (lengths[x][y] == lengths[x][y - 1])

				y--;
			else {
				assert a.charAt(x - 1) == b.charAt(y - 1);

				sb.append(a.charAt(x - 1));

				x--;
				y--;
			}
		}

		return sb.reverse().toString();

	}

	static List<String> readDictionary(String fileName)
			throws FileNotFoundException {
		Scanner sc = new Scanner(new FileReader(fileName));
		List<String> dictionary = new ArrayList<String>();

		while (sc.hasNextLine()) {
			String word = sc.nextLine();
			dictionary.add(word);
		}
		return dictionary;
	}

	static String searchInDictionary(String inputString, List<String> dictionary) {
		String maxWord = "";
		int score = 0;
		String dictionaryWord, prev = "";

		int max = Integer.MIN_VALUE;
		for (int i = 0; i < dictionary.size(); i++) {
			dictionaryWord = dictionary.get(i);

			char letterArray[] = dictionaryWord.toCharArray();
			Arrays.sort(letterArray);
			String newDictionaryWord = new String(letterArray);

			String matchedString = lcs(inputString, newDictionaryWord);
			if (matchedString.length() == newDictionaryWord.length()) {

				score = calculateScore(matchedString);
			} else {

				if (matchedString.length() == newDictionaryWord.length()
						|| matchedString.length() == newDictionaryWord.length() - 1) {

					score = calculateScore(matchedString);
				}

			}

			if (score > max) {
				maxWord = dictionaryWord;
				max = score;
				prev = dictionaryWord;
			}

		}

		return new String(prev + "," + max);
	}

	static int calculateScore(String word) {
		int sum = 0;
		int score[] = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1,
				1, 1, 1, 4, 4, 8, 4, 10 };

		for (int i = 0; i < word.length(); i++) {
			sum += score[word.charAt(i) - 'A'];
		}
		return sum;
	}

	public static void printResult(String input, String result) {

		String[] splitString = result.split(",");
		System.out.println(input + " " + splitString[0] + " " + splitString[1]);

	}

	public static String getWordsWithFixedAlpha(String alpha, int length,
			int position, List<String> dictionary) {
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

	}

	public static String getAlpha(String input) {
		int i = 0;
		for (i = 0; i < input.length(); i++) {
			if (input.charAt(i) != '_')
				break;
		}

		return input.charAt(i) + "";
	}

	public static int getAlphaPosition(String alpha, String scrambledWord) {
		return scrambledWord.indexOf(alpha);
	}

	public static void main(String[] args) throws FileNotFoundException {

		String alpha;
		int length;
		int position;

		List<String> dictionary = new ArrayList<String>();
		String fileName = "sowpods.txt";
		dictionary = readDictionary(fileName);
		Scanner sc = new Scanner(new FileReader("testScramble.txt"));

		while (sc.hasNextLine()) {
			String scrambledWord = sc.nextLine();
			String[] input = scrambledWord.split("[_]");
			if (input.length > 1) {

				alpha = getAlpha(scrambledWord);
				position = getAlphaPosition(alpha, scrambledWord);
				length = scrambledWord.length();
				String words = getWordsWithFixedAlpha(alpha, length, position,dictionary);
				printResult(scrambledWord, words);
			} 
			else {

				char letterArray[] = scrambledWord.toUpperCase().toCharArray();
				Arrays.sort(letterArray);
				String inputString = new String(letterArray);
				String result = searchInDictionary(inputString, dictionary);
				printResult(scrambledWord, result);
			}

		}
		
		sc.close();

	}
}