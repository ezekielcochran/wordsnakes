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
// Operational Status: Functional but not elegant
///////////////////////////////////////////////////////////////////////

// In theory this is on test branch 1 (and this should be the only change)

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class cochran02 {
    final static int MINIMUM_OVERLAP = 2;
    static int[][] connections;
    public static void main(String[] args) {
        // String[] words = {"Alas", "Blend", "Eloped", "Eternal", "Gerund", "Merger", "Pediatric", "Penultimate", "Stinger", "Sudden", "Trice", "Underdeveloped", "yes"};
        // String[] words = {"Apple", "cap", "lemur", "Problem"};
        String[] words = inputWords();
        arrayToLowerCase(words);
        Arrays.sort(words);
        connections = buildConnectionArray(words);
        // printMatrix(connections, true);
        ScoreAndPath hail_mary = driver();
        printSolution(hail_mary.path, words);
        System.out.printf("Score: %d\n", hail_mary.score);
        
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
            thisResult = recursive_score(i, 0, initial_previously_visited);
            if (thisResult.score > max_score) {
                finalResult = thisResult;
                finalResult.path.add(i);
                max_score = thisResult.score;
            }
        }
        return finalResult;
    }
    
    private static ScoreAndPath recursive_score(int starting_point, int reward, ArrayList<Integer> previously_visited) {
        int[] exits = connections[starting_point];
        int max_exit_score = 0;
        ScoreAndPath best = new ScoreAndPath();
        best.path = new ArrayList<Integer>(); 
        for (int i = 0; i < exits.length; i++) {
            int exit_reward = exits[i];
            if (exit_reward >= MINIMUM_OVERLAP * MINIMUM_OVERLAP && !previously_visited.contains(i)) { //CHANGE THIS TO A CONSTANT VARIABLE LATER, FIGURE OUT HOW TO USE IT GLOBALLY
                ArrayList<Integer> visited = new ArrayList<>(previously_visited);
                visited.add(i);
                ScoreAndPath this_exit = recursive_score(i, exit_reward, visited);
                if (this_exit.score > max_exit_score) {
                    max_exit_score = this_exit.score;
                    // System.out.println("this_exit.path: ");
                    // print(this_exit.path);
                    // System.out.println();
                    this_exit.path.add(i);
                    best.path = this_exit.path;
                    // best.path = new ArrayList<Integer>(visited);
                }
            }
        }
        int score = reward + max_exit_score;
        best.score = score;
        return best;
    }

    private static void printSolution(ArrayList<Integer> indexes, String[] words) {
        if (indexes == null) {
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

    private static void printMatrix(int[][] array, boolean hide_zeroes) {
        for (int i = 0; i < array.length; i++) {
            System.out.println();
            for (int j = 0; j < array[i].length; j++) {
                int num = array[i][j];
                if (num == 0 && hide_zeroes) {
                    System.out.print("-  ");
                } else {
                    System.out.printf("%-2d ", num);
                }
            }
        }
        System.out.println("\n");
    } 

    private static void print(ArrayList a) {
        for (int i = 0; i < a.size(); i++) {
            System.out.printf("%d, ", a.get(i));
        }
        System.out.println();
    }
}

class ScoreAndPath {
    int score;
    ArrayList<Integer> path;
}