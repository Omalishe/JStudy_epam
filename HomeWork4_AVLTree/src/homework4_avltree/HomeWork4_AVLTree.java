package homework4_avltree;

import java.util.Comparator;

class MyAVLTree<E>{
    Node root;
    MyComparator comp = new MyComparator();

    public MyAVLTree() {
        //this.root = new Node();
    }

    public MyAVLTree(E data) {
        this.root = new Node(data);
    }

    private void rotateLeft(Node node) {
        if (node==null) return; //can't turn around null
        if (node.right==null) return; //can't turn on null
        
        Node child = node.right;
        if (node.parent!=null){
            if (node.parent.left==node) node.parent.left = child;
            if (node.parent.right==node) node.parent.right = child;
        }
        child.parent = node.parent;

        node.right = child.left;
        node.right.parent=node;

        node.parent = child;
        child.left = node;

        if (child.parent==null) this.root = child;
    }
    
    
    private void rotateRight(Node node){
        if (node==null) return; //can't turn around null
        if (node.left==null) return; //can't turn on null
        Node child = node.left;
        if (node.parent!=null){
            if (node.parent.left==node) node.parent.left = child;
            if (node.parent.right==node) node.parent.right = child;
        }
        child.parent = node.parent;

        node.left = child.right;
        node.left.parent=node;

        node.parent = child;
        child.right = node;

        if (child.parent==null) this.root = child;
    }

      
    public Node add(E element){
        Node returnElement = new Node(element);
        if (this.root==null){ //no need to sift deeper
            this.root = returnElement;
            return returnElement;
        }
        Node current = this.root;
        Node parent = null;
        while (current!=null){
            if (current.data.equals(element)) return current;
            parent = current;
            if (comp.compare(current.data, element)<0) current = current.right;
            else current = current.left;
        }
        
        returnElement.parent = parent;
        if (parent!=null){
            if (comp.compare(parent.data, element)<0) parent.right = returnElement;
            else parent.left = returnElement;
        }else
        {
            this.root = returnElement;
        }
        
        balanceTreeOnAdd(returnElement);
        return returnElement;
    }
    
    public void deleteNode(Node deleted){
        if (deleted==null) return;
        
        Node successor;
        if (deleted.left==null || deleted.right==null){
            successor = deleted;
        }else{
            successor=deleted.right;
            while (successor.left!=null){
                successor = successor.left;
            }
        }
        
        Node onlyChild;
        if (successor.left==null) onlyChild = successor.right;
        else onlyChild=successor.left;
        
        onlyChild.parent = successor.parent; //onlyChild could be a leaf (a null element)
        if (successor.parent!=null){
            if (successor.parent.left == successor) successor.parent.left = onlyChild;
            else successor.parent.right = onlyChild;
        }else {
            this.root = onlyChild;
        }
        
        deleted.data = successor.data;
        
        
    }

    private void balanceTreeOnAdd(Node returnElement) {
        returnElement.balanceParentNode(true);
    }
        
    class MyComparator implements Comparator {
        
        @Override
        public int compare(Object o1, Object o2) {
            int returnValue = 0;
            if (o1.hashCode()>o2.hashCode()) returnValue=1;
            if (o1.hashCode()<o2.hashCode()) returnValue=-1;
            return returnValue;
        }
    }
    
    class Node {

        Node left;
        Node right;
        Node parent;
        E data;
        
        int balanceFactor;
        int treeHeight;
        
        public Node() {
            this(null);
        }
        
        public Node(E data) {
            this.data=data;
        }

        private void balanceParentNode(boolean newElement) {
            int oldTreeHeight = treeHeight;
            int leftHeight = this.left==null?-1:this.left.treeHeight;
            int rightHeight = this.right==null?-1:this.right.treeHeight;
            balanceFactor = rightHeight-leftHeight;
            //make rotations
            if (balanceFactor<-1){ //balance heavy left side
                if (right.treeHeight>1){ //both left and right children are present
                    if(right.left.treeHeight>right.right.treeHeight){
                        
                    }
                }
            }
            if (balanceFactor>1){ //balance heavy right side
                
            }
            treeHeight = leftHeight<rightHeight?rightHeight:leftHeight +1;
            if (((treeHeight!=oldTreeHeight)||newElement)&&(parent!=null)) parent.balanceParentNode(false);
        }
        
    }

    
    
}

public class HomeWork4_AVLTree {

    public static void main(String[] args) {
        MyAVLTree<Integer> mrbt = new MyAVLTree<>();
        MyAVLTree.Node n41 = mrbt.add(41);
        MyAVLTree.Node n38 = mrbt.add(38);
        MyAVLTree.Node n31 = mrbt.add(31);
        MyAVLTree.Node n12 = mrbt.add(12);
        MyAVLTree.Node n19 = mrbt.add(19);
        MyAVLTree.Node n8 = mrbt.add(8);
        
        mrbt.deleteNode(n38);
        mrbt.deleteNode(n19);
        mrbt.deleteNode(n41);
        mrbt.deleteNode(n8);
        mrbt.deleteNode(n12);
        mrbt.deleteNode(n31);
    }
    
}
