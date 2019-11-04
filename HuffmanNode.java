public class HuffmanNode implements Comparable<HuffmanNode> {
  
  private Character inChar = null;
  private int frequency = 0;
  private HuffmanNode left, right;
  
  //constructor
  public HuffmanNode(Character inChar, int frequency, HuffmanNode left, HuffmanNode right){
    
    this.inChar = inChar;
    this.frequency = frequency;
    this.left = left;
    this.right = right;
    
  }
  
  //getter methods for the values of the HuffmanNode
  public Character getChar(){
    return this.inChar;
  }
  
  public int getFrequency(){
    return this.frequency;
  }
  
  public HuffmanNode getLeft(){
    return this.left;
  }
  
  public HuffmanNode getRight(){
    return this.right;
  }
  
  //compareTo method to compare frequencies and follow proper inheritance of interfaces
  public int compareTo(HuffmanNode node){
    return ((Integer)(this.getFrequency())).compareTo((Integer)node.getFrequency());
  }
  
}
