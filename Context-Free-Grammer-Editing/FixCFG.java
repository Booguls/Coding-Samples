//Code written by Kevin Ramirez
//CSC 471-01
//Finished: 5/4/2020

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CSC471Proj2 {

    private static final int S_RULES_INDEX = 0;

    private static ArrayList<ArrayList<String>> CFG = new ArrayList();

    public static void main(String[] args) throws IOException {
        System.out.println("Enter the name of the CFG file without the .txt extension. Current files available:\nCFG1.txt\nCFG2.txt\nCFG3.txt");
        String fileName = new Scanner(System.in).nextLine().toUpperCase().trim();
        System.out.println();

        populateCFG(fileName);  //Populate CFG will every rule set in txt file
        printCFG();

        removeInaccessibleRules();  //Remove all inaccessible rule sets in CFG
        printCFG();

        removeInfiniteRules();  //Remove all infinite rule sets in CFG
        printCFG();
    }

    //Function reads and stores CFG into 2D ArrayList format.
    private static void populateCFG(String fileName) throws FileNotFoundException, IOException {
        System.out.println("Original CFG:");
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName + ".txt")));
        String line;
        while ((line = br.readLine()) != null) {    //Read all lines in text file
            line = line.replaceAll(" ", "");    //Remove all white spaces
            line += '|';    //Append a vertical bar at the end for later use
            ArrayList<String> lineRule = new ArrayList();   //Create a temporary AList to store rules of a line
            lineRule.add(line.charAt(0) + "");  //Add the name of the rule as first AList index

            StringBuilder rule = new StringBuilder();   //Build a string of the rule character by character
            for (int i = 2; i < line.length(); i++) {   //iterate from the character after the dash to the end of the line
                if (line.charAt(i) == '|') {    //If the line is vertical bar, add rule to our AList
                    lineRule.add(rule.toString());
                    rule = new StringBuilder();
                } else {                        //Otherwise, add character to the rule.
                    rule.append(line.charAt(i));
                }
            }
            CFG.add(lineRule);      //Add the rules to the CFG.
        }
    }

    //Functions iterates and removes all inaccessable rules
    private static void removeInaccessibleRules() {
        System.out.println("All inaccessibles removed:");
        boolean[] accessibleRules = new boolean[CFG.size()];    //bool array to note which rules are accessible
        accessibleRules[0] = true;                              //By default, S is accessible
        ArrayList<Character> indexes = new ArrayList();         //AList to easily note which rule name belongs to what index
        for (int i = 0; i < CFG.size(); i++) {
            indexes.add(CFG.get(i).get(0).charAt(0));
        }
        accessor(CFG.get(S_RULES_INDEX), accessibleRules, indexes); //Goal of accessor is to note all rules that can be accessed from S using boolean array
        for (int i = accessibleRules.length - 1; i >= 0; i--) {     //Iterate downward to not disturb earlier elements with element removal process
            if (accessibleRules[i] == false) {                      //If an element is inaccessible, remove it
                CFG.remove(i);
            }
        }
    }

    //Function is used by removeInaccessibleRules() method to recursively check each rule for access
    private static void accessor(ArrayList<String> rules, boolean[] accessList, ArrayList<Character> indexes) {
        for (int i = 1; i < rules.size(); i++) {                //For every rule in the current rule line
            for (int j = 0; j < rules.get(i).length(); j++) {   //For every character in each individual rule, attempt to access if unaccessed.
                char symbol = rules.get(i).charAt(j);
                if (symbol >= (int) 'A' && symbol <= (int) 'Z' && accessList[indexes.indexOf(symbol)] == false) {   //If character is nonTerminal and has not been accessed before
                    accessList[indexes.indexOf(symbol)] = true; //mark nonTerminal as accessible
                    accessor(CFG.get(indexes.indexOf(symbol)), accessList, indexes);    //Go to nonTerminal's rules
                }
            }
        }
    }

    //Function removes all infinites by checking every rule in ruleset contains its own nonTerminal
    private static void removeInfiniteRules() {
        System.out.println("All infinites removed:");
        boolean[] infinites = new boolean[CFG.size()];  //bool array to note which rules are infinite, by default S is not infinite
        boolean[] visited = new boolean[CFG.size()];    //bool array to note which rule set has been visited
        ArrayList<Character> indexes = new ArrayList(); //AList to easily note which rule belongs to what index
        for (int i = 0; i < CFG.size(); i++) {
            indexes.add(CFG.get(i).get(0).charAt(0));
        }
        iterator(CFG.get(S_RULES_INDEX), infinites, indexes, visited); //Goal of iterator is to mark off rule sets which have all rules refering to it's entry nonTerminal
        for (int i = infinites.length - 1; i >= 0; i--) {   //Remove each rule set that is infinite along with all reference to the entry nonTerminal in other rule sets
            if (infinites[i] == true) {
                char nonTerminalToRemove = CFG.get(i).get(0).charAt(0); //Note which nonTerminal we must remove in other rule sets
                CFG.remove(i);  //Remove infinite rule set
                for (ArrayList<String> ruleSet : CFG) { //For every rule set in CFG
                    for (int j = ruleSet.size() - 1; j >= 0; j--) { //For every rule in each rule set (iterated downwards)
                        if (ruleSet.get(j).contains(nonTerminalToRemove + "")) {    //If rule in rule set contains nonTerminalToRemove, remove rule entirely
                            ruleSet.remove(j);
                        }
                    }
                }
            }
        }
    }

    private static void iterator(ArrayList<String> rules, boolean[] infinitesList, ArrayList<Character> indexes, boolean[] visited) {
        //Check if rules lead to infinite
        char entryNonTerminal = rules.get(0).charAt(0); //Non terminal to check for (nonTerminal which grants access to rule set)
        visited[indexes.indexOf(entryNonTerminal)] = true;  //Note this rule set has been visited
        int nonSelfLoops = rules.size() - 1;                 //Number of rules in rule set, if all rules contain the entryRule then rule set will always lead to infinite
        for (int i = 1; i < rules.size(); i++) {        //Start at one to exclude the entry nonTerminal
            if (rules.get(i).contains(entryNonTerminal + "")) { //If rule contains entry nonTerminal, decrement nonSelfLoop count to know how many rules left could be non infinite
                nonSelfLoops--;
            }
        }
        if (nonSelfLoops == 0) {    //If every rule in rule set referenced entry nonTerminal, then rule set is infinite
            infinitesList[indexes.indexOf(entryNonTerminal)] = true;
        }

        for (int i = 1; i < rules.size(); i++) {    //for every rule in rule set, check for other nonTerminals in rule set and check for infinites
            for (char c : rules.get(i).toCharArray()) {
                if (c >= (int) 'A' && c <= (int) 'Z' && infinitesList[indexes.indexOf(c)] == false && visited[indexes.indexOf(c)] == false) {
                    iterator(CFG.get(indexes.indexOf(c)), infinitesList, indexes, visited);
                }
            }
        }
    }

    //Function that prints entire CFG, please note column 0 represents entry Non terminal
    private static void printCFG() {
        for (int i = 0; i < CFG.size(); i++) {
            System.out.println(CFG.get(i).toString());
        }
        System.out.println();
    }
}
