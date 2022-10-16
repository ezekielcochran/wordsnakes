///////////////////////////////////////////////////////////////////////
// Student name: Ezekiel Cochran
// Course: COSC 3523 Section 01 - Analysis of Algorithms
// Assignment: #02 - Wordsnakes
// Filename: cochran02.java
//
// Purpose: Find the optimal wordsnake given a list of words
//
// Assumptions: None known
//
// Limitations: Requires input to start with an integer corresponding to the number of words in the input list
// 
// Development Computer: 2020 MacBook Pro
// Operating System: macOS Monterey 12.0.1
// Compiler:  java
// Integrated Development Environment (IDE): Visual Studio Code
// Operational Status: Functional, a little more elegant
///////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class cochran02 {
    final static int MINIMUM_OVERLAP = 2;
    static int[][] connections;
    public static void main(String[] args) {
        String[] words = inputWords();
        arrayToLowerCase(words);
        Arrays.sort(words);
        connections = buildConnectionArray(words);
        ScoreAndPath solution = driver();
        printSolution(solution.path, words);
        System.out.printf("Score: %d\n", solution.score);
    }

    private static String[] inputWords() {
        System.out.print("Enter number of words to be considered: ");
        Scanner keyboard = new Scanner(System.in);
        int length = keyboard.nextInt() + 1;
        String[] words = new String[length];
        System.out.println("Enter one word at a time, separated by newlines: ");
        for (int i = 0; i < length; i++) {
            words[i] = keyboard.nextLine();
        }
        keyboard.close();
        return words;
    }

    private static ScoreAndPath driver() {
        int max_score = 0;
        ArrayList<Integer> initial_previously_visited = new ArrayList<Integer>();
        ScoreAndPath thisResult = new ScoreAndPath();
        ScoreAndPath finalResult = new ScoreAndPath();
        for (int i = 0; i < connections.length; i++) {
            thisResult = recursive_score(i, initial_previously_visited);
            if (thisResult.score > max_score) {
                finalResult = thisResult;
                finalResult.path.add(i);
                max_score = thisResult.score;
            }
        }
        return finalResult;
    }

    private static ScoreAndPath recursive_score(int starting_point, ArrayList<Integer> previously_visited) {
        int[] exits = connections[starting_point];
        ScoreAndPath best = new ScoreAndPath();
        for (int i = 0; i < exits.length; i++) {
            int connection_score = exits[i];
            if (connection_score >= MINIMUM_OVERLAP * MINIMUM_OVERLAP && !previously_visited.contains(i)) {
                ArrayList<Integer> visited = new ArrayList<Integer>(previously_visited);
                visited.add(i);
                ScoreAndPath this_exit = recursive_score(i, visited);
                int this_score = connection_score + this_exit.score;
                if (this_score > best.score) {
                    best.score = this_score;
                    this_exit.path.add(i);
                    best.path = this_exit.path;
                }
            }
        }
        return best;
    }

    private static void printSolution(ArrayList<Integer> indexes, String[] words) {
        if (indexes == null || indexes.size() == 0) { //Added second part of condition, didn't use to be necessary
            System.out.println("\n\nNo Wordsnakes Exist.\n");
        } else {
            System.out.print("\n\nBest Wordsnake:\n\n");
            for (int i = indexes.size() - 1; i >= 0; i--) {
                System.out.println(words[indexes.get(i)]);
            }
            System.out.println();
        }
    }

    private static int[][] buildConnectionArray(String[] sorted_word_list) {
        int[][] result = new int[sorted_word_list.length][sorted_word_list.length];
        for (int i = 0; i < sorted_word_list.length; i ++) {
            String first = sorted_word_list[i];
            for (int j = 0; j < sorted_word_list.length; j++) {
                int score = score_overlap(first, sorted_word_list[j]);
                result[i][j] = score * score;
            }
        }
        return result;
    }

    private static int score_overlap(String first_word, String second_word) {
        if (first_word.equals(second_word)) {
            return 0;
        }
        char[] first_chars = first_word.toCharArray();
        char[] second_chars = second_word.toCharArray();
        for (int i = 0; i < first_chars.length; i++) {
            if (test_equality(first_chars, second_chars, i)) {
                return first_chars.length - i;
            }
        }
        return 0;
    }

    private static Boolean test_equality(char[] first, char[] second, int start_index) {
        if (first.length == 0 || second.length == 0) {
            return false;
        }
        int j = 0;
        for (int i = start_index; i < first.length; i++) {
            if (j >= second.length) {
                return false;
            }
            if (first[i] != second[j]) {
                return false;
            }
            j++;
        }
        return true;
    }

    private static void arrayToLowerCase(String[] string_list) {
        for (int i = 0; i < string_list.length; i++) {
            string_list[i] = string_list[i].toLowerCase();
        }
    }
}

class ScoreAndPath {
    int score = 0;
    ArrayList<Integer> path = new ArrayList<Integer>();
}