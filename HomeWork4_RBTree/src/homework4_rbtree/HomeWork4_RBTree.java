package homework4_rbtree;

import java.util.Comparator;

class MyRBTree<E>{
    Node root;
    MyComparator comp = new MyComparator();

    public MyRBTree() {
        //this.root = new Node();
        //this.root.color = NodeColor.BLACK;
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
    
    public MyRBTree(E data) {
        this.root = new Node(data);
        this.root.color = NodeColor.BLACK;
    }

    private void rotateLeft(Node node) {
        if (node==null) return; //can't turn around null
        if (node.isLeaf) return; //can't turn around leaf
        if (node.right.isLeaf) return; //can't turn on leaf;
        
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
        if (node.isLeaf) return; //can't turn around leaf
        if (node.left.isLeaf) return; //can't turn on leaf;
        
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

    private void balanceTreeOnDelete(Node node) {
        while (node!=root && node.color==NodeColor.BLACK){
            if (node.parent.left == node){
                Node sibling = node.parent.right;
                if (sibling.color==NodeColor.RED){
                    sibling.color=NodeColor.BLACK;
                    sibling.parent.color=NodeColor.RED;
                    rotateLeft(sibling.parent);
                    sibling = node.parent.right;
                }
                if (sibling.left.color==NodeColor.BLACK&&sibling.right.color==NodeColor.BLACK){
                    sibling.color=NodeColor.RED;
                    node = node.parent;
                }else{
                    if (sibling.right.color==NodeColor.BLACK){
                        sibling.left.color=NodeColor.BLACK;
                        sibling.color=NodeColor.RED;
                        rotateRight(sibling);
                        sibling = node.parent.right;
                    }
                    sibling.color=node.parent.color;
                    node.parent.color=NodeColor.BLACK;
                    sibling.right.color = NodeColor.BLACK;
                    rotateLeft(node.parent);
                    node = root;
                }
            }else{
                Node sibling = node.parent.left;
                if (sibling.color==NodeColor.RED){
                    sibling.color=NodeColor.BLACK;
                    sibling.parent.color=NodeColor.RED;
                    rotateRight(sibling.parent);
                    sibling = node.parent.left;
                }
                if (sibling.left.color==NodeColor.BLACK&&sibling.right.color==NodeColor.BLACK){
                    sibling.color=NodeColor.RED;
                    node = node.parent;
                }else{
                    if (sibling.left.color==NodeColor.BLACK){
                        sibling.right.color=NodeColor.BLACK;
                        sibling.color=NodeColor.RED;
                        rotateLeft(sibling);
                        sibling = node.parent.left;
                    }
                    sibling.color=node.parent.color;
                    node.parent.color=NodeColor.BLACK;
                    sibling.left.color = NodeColor.BLACK;
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }
        node.color=NodeColor.BLACK;
    }
    
    public Node add(E element){
        Node returnElement = new Node(element);
        if (this.root==null){ //no need to sift deeper
            this.root = returnElement;
            this.root.color = NodeColor.BLACK;
            return returnElement;
        }
        Node current = this.root;
        Node parent = null;
        while (!current.isLeaf){
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
    
    public void deleteItem(E data){
        Node deleted = findNodeByData(data);
        deleteNode(deleted);
    }
    
    public void deleteNode(Node deleted){
        if (deleted==null) return;
        if (deleted.isLeaf) return; //leaves are undeletable
        
        Node successor;
        if (deleted.left.isLeaf || deleted.right.isLeaf){
            successor = deleted;
        }else{
            successor=deleted.right;
            while (!successor.left.isLeaf){
                successor = successor.left;
            }
        }
        
        Node onlyChild;
        if (successor.left.isLeaf) onlyChild = successor.right;
        else onlyChild=successor.left;
        
        onlyChild.parent = successor.parent; //onlyChild could be a leaf (a null element)
        if (successor.parent!=null){
            if (successor.parent.left == successor) successor.parent.left = onlyChild;
            else successor.parent.right = onlyChild;
        }else {
            this.root = onlyChild;
        }
        
        deleted.data = successor.data;
        if (successor.color==NodeColor.BLACK){
            balanceTreeOnDelete(onlyChild); 
        }
        
    }
        
    private void balanceTreeOnAdd(Node node){
        
        while (node!=this.root && node.parent.color==NodeColor.RED){
            Node parent=node.parent;
            Node gp = parent.parent;
            if(parent == gp.left){
                Node uncle = gp.right;
                if (uncle.color==NodeColor.RED){
                    parent.color=NodeColor.BLACK;
                    uncle.color=NodeColor.BLACK;
                    gp.color = NodeColor.RED;
                    node = gp;
                }else{
                    if (node==parent.right){
                        node=parent;
                        rotateLeft(node);
                    }
                    node.parent.color=NodeColor.BLACK;
                    node.parent.parent.color=NodeColor.RED;
                    rotateRight(node.parent.parent);
                }
            }else{
                Node uncle = gp.left;
                if (uncle.color==NodeColor.RED){
                    parent.color=NodeColor.BLACK;
                    uncle.color=NodeColor.BLACK;
                    gp.color = NodeColor.RED;
                    node = gp;
                }else{
                    if (node==parent.left){
                        node=parent;
                        rotateRight(node);
                    }
                    node.parent.color=NodeColor.BLACK;
                    node.parent.parent.color = NodeColor.RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        this.root.color=NodeColor.BLACK;
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
        NodeColor color;
        boolean isLeaf;
        E data;

        public Node() {
            this(null);
        }
        
        public Node(E data) {
            this.color=NodeColor.RED;
            this.data=data;
            this.isLeaf = false;
            this.left = new Node(this, true);
            this.right = new Node(this,true);
        }
        
        public Node(Node parent, boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.left = null;
            this.data = null;
            this.right = null;
            this.parent = parent;
            this.color = NodeColor.BLACK;
        }
    }

    enum NodeColor {

        RED, BLACK
    }
    
    
}

public class HomeWork4_RBTree {

    public static void main(String[] args) {
        MyRBTree<Integer> mrbt = new MyRBTree<>();
        MyRBTree.Node n41 = mrbt.add(41);
        MyRBTree.Node n38 = mrbt.add(38);
        MyRBTree.Node n31 = mrbt.add(31);
        MyRBTree.Node n12 = mrbt.add(12);
        MyRBTree.Node n19 = mrbt.add(19);
        MyRBTree.Node n8 = mrbt.add(8);
        
        mrbt.deleteItem(38);
        mrbt.deleteItem(19);
        mrbt.deleteItem(41);
        mrbt.deleteItem(8);
        mrbt.deleteItem(12);
        mrbt.deleteItem(31);
    }
    
}
