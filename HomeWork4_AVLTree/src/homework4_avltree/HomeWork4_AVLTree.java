package homework4_avltree;

import java.util.Comparator;

class MyAVLTree<E>{
    Node root;
    MyComparator comp = new MyComparator();

    public MyAVLTree() {
        //this.root = new Node();
    }
    
    public Node findNodeByData(E data){
        Node returnElement = null;
        Node current = this.root;
        while (current!=null){
            if (current.data.equals(data)) return current;
            returnElement = current;
            if (comp.compare(current.data, data)<0) current = current.right;
            else current = current.left;
        }
        return returnElement;
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
        if (node.right!=null) node.right.parent=node;

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
        if (node.left!=null) node.left.parent=node;

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
        
        balanceTree(returnElement);
        return returnElement;
    }
    
    public void deleteItem(E data){
        Node deleted = findNodeByData(data);
        deleteNode(deleted);
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
        
        if (onlyChild!=null) onlyChild.parent = successor.parent; //onlyChild could be a leaf (a null element)
        if (successor.parent!=null){
            if (successor.parent.left == successor) successor.parent.left = onlyChild;
            else successor.parent.right = onlyChild;
        }else {
            this.root = onlyChild;
        }
        
        deleted.data = successor.data;
        balanceTree(successor);
        
        
    }

    private void balanceTree(Node balanceNode) {
        Node parent = balanceNode.parent; //new node is balanced by default, so we start from its parent
        while(parent!=null){
            //int oldTreeHeight = parent.treeHeight;
            parent.restoreBalanceFactor();
            if (parent.balanceFactor<-1){ //balance heavy left side
                if (parent.left.balanceFactor>=1){ //heavy right subtree of left tree - do big turn
                    Node pl = parent.left;
                    rotateLeft(pl);
                    pl.restoreBalanceFactor();
                    pl.restoreHeight();
                }
                rotateRight(parent); //TODO: restore height and balanceFactor
                parent.restoreBalanceFactor();
            }
            if (parent.balanceFactor>1){ //balance heavy right side
                if (parent.right.balanceFactor<=-1){//heavy left subtree of right tree - do big turn
                    Node pr = parent.right;
                    rotateRight(pr);
                    pr.restoreBalanceFactor();
                    pr.restoreHeight();
                }
                rotateLeft(parent); //TODO: restore height and balanceFactor
                parent.restoreBalanceFactor();
            }
            parent.restoreHeight();
            
            //if (oldTreeHeight==parent.treeHeight) break; // if height isn't changed, everything up from here is already balanced
            
            parent = parent.parent;
        }
    }
        
    class MyComparator implements Comparator {
        
        @Override
        public int compare(Object o1, Object o2) {
            int returnValue = 0;
            if (o1.hashCode()>o2.hashCode()) returnValue= 1;
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

        private void restoreBalanceFactor() {
            int leftHeight = left==null?-1:left.treeHeight;
            int rightHeight = right==null?-1:right.treeHeight;
            balanceFactor = rightHeight-leftHeight;
        }

        private void restoreHeight() {
            int leftHeight = left==null?-1:left.treeHeight;
            int rightHeight = right==null?-1:right.treeHeight;
            treeHeight = (leftHeight<rightHeight?rightHeight:leftHeight) +1;
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
        
        mrbt.deleteItem(38);
        mrbt.deleteItem(19);
        mrbt.deleteItem(41);
        mrbt.deleteItem(8);
        mrbt.deleteItem(12);
        mrbt.deleteItem(31);
    }
    
}
