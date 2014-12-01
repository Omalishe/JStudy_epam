package homework7_huffman;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class Node{

    public Node(String word, int freq, Node left, Node right, boolean isLeaf, String byteCode) {
        this.word = word;
        this.freq = freq;
        this.left = left;
        this.right = right;
        this.isLeaf = isLeaf;
        this.byteCode = byteCode;
    }
    
    String word;
    int freq;
    Node left;
    Node right;
    boolean isLeaf;
    String byteCode;
}

class FreqComparator implements Comparator<Node>{

    @Override
    public int compare(Node o1, Node o2) {
        return (int)(o1.freq - o2.freq);
    }
    
}

public class HomeWork7_Huffman {
    static final String fName = "";
    public static void main(String[] args) throws FileNotFoundException, IOException {
        //reading file to array of words
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/oleksandr/tale")));
        String lineFeed;
        StringBuilder sb = new StringBuilder();
        while((lineFeed=br.readLine())!=null){
            sb=sb.append(lineFeed).append("\n");
        }
        lineFeed = sb.toString();
        
//building the frequency diagram using hashmap, where key is the word and value is freq
        String[]arr = lineFeed.split("((?<=[ !?#,.<>/\\|%*()\n])|(?=[ !?#,.<>/\\|%*()\n]))");
        Map<String,Integer> diagram = new HashMap<>();
        for(String a:arr){
            diagram.merge(a, 1, Integer::sum);
        }
        
        //building the Huffman-tree using priority queue
        Comparator<Node> comp= new FreqComparator();
        PriorityQueue<Node> pq = new PriorityQueue<>(comp);
        for (Map.Entry<String,Integer> e:diagram.entrySet()){
            pq.add(new Node(e.getKey(),e.getValue(),null,null,true,null));
        }
        
        int size = pq.size();
        Map<String,String> codeTree = new HashMap<>(size);
        while (size>1){
            Node left = pq.poll();
            Node right = pq.poll();
            pq.add(new Node("",left.freq+right.freq,left,right,false,null));
            size = pq.size();
        }
        Node root = pq.peek();
        traverseTree(root, codeTree, "0");
        
        //output codes
        StringBuilder out = new StringBuilder();
        for(String a:arr){
            out.append(codeTree.get(a));
        }
        StringBuilder compressedText = new StringBuilder();
        int lengthOfText = out.length();
        
        byte tempChar=0;
        
        for (int i=0;i<lengthOfText;i++){
            tempChar=(byte)(tempChar|Byte.parseByte(out.substring(i, i+1)));
            if ((i+1)%8==0&&i!=0){
                compressedText.append((char)tempChar);
                tempChar = 0;
            }
            tempChar=(byte)(tempChar<<1);
        }
        
        
        
        System.out.println(compressedText);
        //System.out.println(out.toString());
        String toDecompress = compressedText.toString();
        int decompLentgh = toDecompress.length();
        StringBuilder out2 = new StringBuilder();
        
        for(int i=0;i<decompLentgh;i++){
            char a = toDecompress.charAt(i);
            for (int j=0;j<8;j++){
                byte mask = (byte)(128>>j);
                byte value = (byte)((byte)a&mask);
                if (value==0) out2.append("0"); else out2.append("1");
            }
        }
        out = out2;//.toString();
        
        
        //reversing code tree to search strings for codes
        Map<String,String> searchCodeTree = new HashMap<>(size);
        for (Map.Entry<String,String> e:codeTree.entrySet()) searchCodeTree.put(e.getValue(), e.getKey());
        
        StringBuilder restoredText = new StringBuilder();
        StringBuilder buffer = new StringBuilder();
        
        lengthOfText = out.length();
        for (int i=0;i<lengthOfText;i++){
            buffer.append(out.charAt(i));
            if (searchCodeTree.containsKey(buffer.toString())){
                restoredText.append(searchCodeTree.get(buffer.toString()));
                buffer.delete(0, buffer.length());
            }
        }
        System.out.println(restoredText);
    }
    
    private static void traverseTree(Node node, Map<String,String> map, String code){
        if (node.isLeaf) {
            map.put(node.word, code);
            node.byteCode = code;
        }else{
            traverseTree(node.left, map, code+"0");
            traverseTree(node.right, map, code+"1");
        }
    }
}
