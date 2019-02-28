/*
    Kevin Ramirez
    Amlan Chatterjee
    CSC 311-01

    StringNode is a simple node class which contains a String and a reference to another StringNode object.
    Used in TextSuggestions.
 */
public class StringNode {

    StringNode next;
    String data;

    public StringNode(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StringNode{" + "data = " + data + '}';
    }

}
