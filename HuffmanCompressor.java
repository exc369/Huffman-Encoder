import java.util.*;
import java.io.*;

public class HuffmanCompressor {
  
  //A hashMap containing the letter and frequencies of each letter
  private static HashMap<Character, Integer> map = new HashMap<Character, Integer>();
  //A priority queue that carries the ordered letters in terms of frequency
  private static PriorityQueue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>();
  //A list to sort the letters in terms of frequency
  private static ArrayList<HuffmanNode> list = new ArrayList<HuffmanNode>();
  //A representation of the root node of the Huffman Tree
  private static HuffmanNode rootNode;
  //A representation of the encoded letters in reduced bit form
  private static HashMap<Character, String> encodedLetters = new HashMap<Character, String>();
  
  //A variable to hold the total number of bits in the inputted file
  private static int totalBits = 0;
  //A variable to hold the total number of bits in the reduced, written file
  private static int reducedBits = 0;
  
  //A method to encode the inputted file which is then rewritten into a new file
  public static void huffmanCoder(String inputFileName, String outputFileName) throws FileNotFoundException {
    //scanner object to read each individual word
    Scanner scanner = new Scanner(new File(inputFileName));
    //string builder object to create and assign the new encoded bit values. Not used until line 57
    StringBuilder s = new StringBuilder();
    
    //parsing through the file to compile each letter and record the frequencies
    while (scanner.hasNext()) {
      String word = scanner.next();
      for (int i = 0; i < word.length(); i++) {
        if (Character.toLowerCase(word.charAt(i)) >= 'a' && (Character.toLowerCase(word.charAt(i)) <= 'z')) {
          if (map.get(Character.toLowerCase(word.charAt(i))) == null) {
            map.put(Character.toLowerCase(word.charAt(i)), 1);
          } else {
            map.put(Character.toLowerCase(word.charAt(i)), map.get(Character.toLowerCase(word.charAt(i))) + 1);
          }
        }
      }
    }
    //set of characters to hold the map of characters, frequencies
    Set<Character> chars = map.keySet();
    
    for (Character c : chars) {
      //assigning each letter to a HuffmanNode with the respective frequencies acquired from above
      HuffmanNode node = new HuffmanNode(c, map.get(c), null, null);
      //adding each node to the list to be sorted
      list.add(node);
    }
    
    //calling the helper insertion sort method written below on list
    sort(list);
    
    //for loop to add every node in the list to a priorityQueue
    for (int i = 0; i < list.size(); i++) {
      queue.add(list.get(i));
      //Calculating the total number of bits in the inputted file
      totalBits += (list.get(i).getFrequency()) * 8;
    }
    
    //creating the HuffmanTree from the priority queue and setting the root to rootNode
    rootNode = tree(queue);
   
    System.out.println("The Number of Leafs in this tree is: " + getLeafCount(rootNode));
    System.out.println("The Max Depth or Height of this tree is: " + maxDepth(rootNode));
    
    //assigning new encoded bit values to each node in the tree based on the tree, using stringbuilder s to hold the bits
    assignBits(rootNode, s);
    
    //printing out the key values
    for (int i = 0; i < list.size(); i++) {
      System.out.println(list.get(i).getChar() + " : " + list.get(i).getFrequency() + " : " + encodedLetters.get(list.get(i).getChar()));
    }
    
    //try catch block to ensure that the name of the file does not already exists. Also catches if the file name cannot be found
    try {
      //calling the encode helper method with respective parameters
      encode(inputFileName, outputFileName);
    } catch (Exception e) {
      System.out.println("This file already exists or the name of the input file cannot be found");
    }
    System.out.println("The total amount of bits in the input file is : " + totalBits);
    System.out.println("The total amount of bits in the new written file is : " + reducedBits);
    System.out.println("The total amount of \"space\" conserved is : " + (totalBits - reducedBits) + " Bits");
    scanner.close();
  }
  
  //helper method that is essentially a insertion sort method to sort the list from least to greatest frequencies
  public static void sort(ArrayList<HuffmanNode> list) {
    int i, j;
    for (i = 1; i < list.size(); i++) {
      HuffmanNode ptr = list.get(i);
      j = i;
      while ((j > 0) && (list.get(j - 1).getFrequency() > ptr.getFrequency())) {
        list.set(j, list.get(j - 1));
        j--;
      }
      list.set(j, ptr);
    }
  }
  
  //helper method to count the leafs in the HuffmanNode tree
  public static int getLeafCount(HuffmanNode node) {
    if (node == null)
      return 0;
    if (node.getLeft() == null && node.getRight()== null)
      return 1;
    else
      return getLeafCount(node.getLeft()) + getLeafCount(node.getRight());
  }
  
  //helper method to calculate the maximum height or depth of the HuffmanNode tree
  public static int maxDepth(HuffmanNode node)  
  { 
    if (node == null) 
      return 0; 
    else 
    { 
      int lDepth = maxDepth(node.getLeft()); 
      int rDepth = maxDepth(node.getRight()); 
      if (lDepth > rDepth) 
        return (lDepth + 1); 
      else 
        return (rDepth + 1); 
    } 
  } 
  
  //helper method to merge 2 HuffmanNodes.
  public static HuffmanNode merge(HuffmanNode n, HuffmanNode m){
    HuffmanNode node = new HuffmanNode(null, (n.getFrequency() + m.getFrequency()), n, m);
    return node;
  }
  
  //helper method to create the HuffmanNode tree using recurtion and priority queue methods from the api
  public static HuffmanNode tree(PriorityQueue<HuffmanNode> pq){
    if (pq.size() <= 1){
      return pq.poll();
    }
    else{
      pq.add(merge(pq.poll(), pq.poll()));
      return tree(pq);
    }
  }
  
  //helper method to assign a new, encoded bit value to different letters
  public static void assignBits(HuffmanNode node, StringBuilder s){
    if (node != null) {
      if (node.getLeft() == null && node.getRight() == null) {
        encodedLetters.put(node.getChar(), s.toString());
      } else {
        s.append('0');
        assignBits(node.getLeft(), s);
        s.deleteCharAt(s.length() - 1);
        
        s.append('1');
        assignBits(node.getRight(), s);
        s.deleteCharAt(s.length() - 1);
      }
    }
  }
  
  //helper method that contain code to encode the inputted file and write a new file
  public static void encode(String inputFileName, String outputFileName) throws IOException{
    
    Scanner scanner = new Scanner(new File(inputFileName));
    File file = new File(outputFileName);
    //if statement to check if the file already exists
    //writer class to edit the file created
    FileWriter editor = new FileWriter(file, true);
    while(scanner.hasNext()) {
      String word = scanner.next();
      for (int i = 0; i < word.length(); i ++) {
        if (encodedLetters.containsKey(word.charAt(i))) {
          //getting each encoded bit value
          String bits = encodedLetters.get(word.charAt(i));
          //calculating the new encoded bit value to be used in computing "space saved"
          reducedBits += bits.length();
          //writing each encoded bit value onto the new file
          editor.write(bits);
        }
      }
    }
    scanner.close();
    editor.close();
  }
  public static void main(String[] args) throws IOException{
    //main method to ensure proper commands can be executed to test in the form of "java HuffmanCompressor inputFile outputFile"
    HuffmanCompressor.huffmanCoder(args[0], args[1]);
    
  }
}
