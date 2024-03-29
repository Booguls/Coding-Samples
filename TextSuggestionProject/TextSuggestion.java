/*
    Kevin Ramirez
    Amlan Chatterjee
    CSC 311-01

    Methods SortDictionary and MergeDicts are implementations of the psuedo code
    found on tutorialpoints.com:
    https://www.tutorialspoint.com/data_structures_algorithms/merge_sort_algorithm.htm

    This program uses a dictionary of words to provide correct spellings of user typed words.
    The program can only detect anagram mispellings, such as "gaem" and provide all correct spellings for the given string.
    Example:    Input: gaem
                Expected Output: Suggestions: egma game mage
                Output: Suggestions: Suggestions: egma game mage

    KNOWN BUGS: If the user types a word which is correctly spelled and is the last item in a given keycode linked list, then it will suggest all possible corrections.
    Example:    Input: mage 
                Expected output: Correct.
                Actual output: Suggestions: egma game mage
    Bug is expected to be in ReadUserStrings().
 */
import java.io.*;
import java.util.*;

public class TextSuggestion {

    private static final String END_INPUT = "DONE";
    private static final String PATH1 = "C:\\Users\\";
    private static final String PATH2 = "\\Documents\\NetbeansProjects\\DataStructuresProjects\\build\\classes\\Project2\\dictionary.txt";

    public static void main(String[] args) throws IOException {
        String[] dictionary = ReadTextFile();       //ReadTextFile() reads the dictionary text file and insert it into a string array.
        System.out.println("Sorting...");
        dictionary = SortDictionary(dictionary);    //SortDictionary() sorts the string array of dictionary words in alphabetical order.
        System.out.println("Sorting done!\nCreating HashMap...");
        HashMap<Integer, StringNode> hmap = HashDict(dictionary);   //HashDict() will create a HashMap of the given dictionary.
        System.out.println("Hashing done!");
        ReadUserStrings(hmap);                      //ReadUserStrings() compares the user string input to the HashMap to detect anagrams and provide suggested words.
    }

    public static String[] ReadTextFile() throws FileNotFoundException, IOException {
        ArrayList<String> dynamicList = new ArrayList<>();      //Create an ArrayList to input strings in given dictionary of variable length.
        System.out.print("Enter the name of your pc to access the embedded dictionary: ");
        String compUser = new Scanner(System.in).nextLine();
        BufferedReader reader = new BufferedReader(new FileReader(new File(PATH1 + compUser + PATH2))); //Request user computer name for access to the dictionary directory.
        String currentLine = reader.readLine();     //Create a string to read the text file line by line.
        while (currentLine != null) {
            dynamicList.add(currentLine);
            currentLine = reader.readLine();
        }
        System.out.println("Dictionary size: " + dynamicList.size() + " words");    //Print the size of the dictionary.
        return dynamicList.toArray(new String[0]);
    }

    public static String[] SortDictionary(String[] dict) {
        if (dict.length == 0) {      //Base case to check if the dictionary size is greater than 1 to sort.
            System.out.println("Error! Dictionary cannot be sorted!");
            return null;
        } else if (dict.length == 1) {
            return dict;
        }
        int mid = dict.length / 2;      //Standard merge sort technique to sort the given dictionary in O(nlog(n)) time.
        String[] L1 = new String[mid];  //Split input array into two new arrays containing half the dictionary each.
        String[] L2 = new String[dict.length - mid];
        for (int i = 0; i < mid; i++) {
            L1[i] = dict[i];
        }
        for (int i = mid, j = 0; i < dict.length; i++) {
            L2[j] = dict[i];
            j++;
        }

        L1 = SortDictionary(L1);    //Recursively split both arrays in half.
        L2 = SortDictionary(L2);

        return MergeDicts(L1, L2);  //Merge the two arrays in MergeDicts() method.
    }

    public static String[] MergeDicts(String[] dict1, String[] dict2) { //Method to merge the two dictionaries in one, return merged string.
        String[] mergedList = new String[dict1.length + dict2.length];
        int ptrDict1 = 0;
        int ptrDict2 = 0;
        int ptrMerged = 0;
        while (ptrDict1 < dict1.length && ptrDict2 < dict2.length) {    //Merge both arrays into one in alpphabetical order.
            if (dict1[ptrDict1].compareTo(dict2[ptrDict2]) > 0) {
                mergedList[ptrMerged] = dict2[ptrDict2];
                ptrMerged++;
                ptrDict2++;
            } else {
                mergedList[ptrMerged] = dict1[ptrDict1];
                ptrMerged++;
                ptrDict1++;
            }
        }

        while (ptrDict1 < dict1.length) {
            mergedList[ptrMerged] = dict1[ptrDict1];
            ptrMerged++;
            ptrDict1++;
        }

        while (ptrDict2 < dict2.length) {
            mergedList[ptrMerged] = dict2[ptrDict2];
            ptrMerged++;
            ptrDict2++;
        }

        return mergedList;
    }

    public static HashMap<Integer, StringNode> HashDict(String[] dict) {
        HashMap<Integer, StringNode> hmap = new HashMap<>();
        int uniqueKeys = 0;
        for (int i = 0; i < dict.length; i++) {
            int hashCode = SortEntry(dict[i].toLowerCase()).hashCode(); //Hash an entry by lowercasing the entry, sorting the string in alphabetical order, and getting the hashcode.
            if (hmap.containsKey(hashCode)) {       //Detect for existing node in hashCode, if true the attach new node to the last node in the list. (Hashmap CHAINING technique).
                StringNode currentNode = hmap.get(hashCode);
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                }
                currentNode.next = new StringNode(dict[i]);     //Attach new node to the end of the last node.
            } else {
                hmap.put(hashCode, new StringNode(dict[i]));    //If no node exist at hashCode, set the new node to the key.
                uniqueKeys++;   //If the entry provides require a new key, increment uniqueKeys.
            }
        }
        System.out.println("Number of unique keys: " + uniqueKeys);
        return hmap;
    }

    public static String SortEntry(String entry) {  //Method to sort the string in alphabetical order (Ex. game -> aegm);
        char[] stringChars = entry.toCharArray();
        Arrays.sort(stringChars);
        return new String(stringChars);
    }

    public static void ReadUserStrings(HashMap<Integer, StringNode> map) {  //Method to take user input and compare it to the dictionary for spelling.
        System.out.println("Enter a series of words. Type DONE when you are finished.");
        Scanner input = new Scanner(System.in);
        String userInput = input.nextLine();
        while (!userInput.equals(END_INPUT)) {      //While method to check if the user typed DONE, if so then exit.
            int hashCode = SortEntry(userInput.toLowerCase()).hashCode();   //Lowercase and alphabetize entry to get the hashCode.
            if (map.containsKey(hashCode)) {        //Check if hashCode key exists on HashMap, if so then look for input string linearly.
                StringNode currentNode = map.get(hashCode); //Create node with reference to hashCode's node.
                boolean foundWord = false;
                while (currentNode.next != null) {        //While loop which compares the current node to the user input, if true then we found our word and the spelling is correct.
                    if (userInput.equalsIgnoreCase(currentNode.data)) {
                        System.out.println("Correct.");
                        foundWord = true;
                        break;
                    } else {
                        currentNode = currentNode.next;
                    }
                }
                if (!foundWord) {   //If foundWord is false, then we print all suggested words for user given anagram.
                    printSuggestions(map, hashCode);
                }
            } else {
                System.out.println("No Suggestions.");  //If user input hashCode does not exist in dictionary, print no suggestions.
            }
            userInput = input.nextLine();
        }
    }

    public static void printSuggestions(HashMap<Integer, StringNode> map, int hashCode) {   //Method which prints all suggested words linearly by single linked list traversal.
        StringNode currentNode = map.get(hashCode);
        String suggestions = "";
        boolean keepRunning = true;
        while (keepRunning) {
            if (currentNode.next == null) {
                suggestions += currentNode.data + " ";
                keepRunning = false;
            } else {
                suggestions += currentNode.data + " ";
                currentNode = currentNode.next;
            }
        }
        System.out.println("Suggestions: " + suggestions);
    }
}
