/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1_knight;

/**
 *
 * @author oleksandr
 */
class Armour implements Comparable<Armour>{
    
    private float price;
    private String name;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    private float weight;

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(Armour o) {
        if (this.weight<o.weight) return -1; else return 1;
    }
    
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Armour(String name, float price, float weight, int size) {
        this.price = price;
        this.name = name;
        this.weight = weight;
        this.size = size;
    }

    public String getName() {
        return name;
    }
    
    public void displaySpecs(){
        System.out.print(getName()+", price: "+getPrice()+", weight: "+getWeight());
    }


}

class ArmArmour extends Armour {
    
    private boolean elbowProtect;

    public ArmArmour(String name, float price, float weight, int size, boolean elbowProtect) {
        super(name, price, weight, size);
        this.elbowProtect = elbowProtect;
    }

    public boolean isElbowProtect() {
        return elbowProtect;
    }

    public void setElbowProtect(boolean elbowProtect) {
        this.elbowProtect = elbowProtect;
    }
    
    @Override
    public void displaySpecs(){
        super.displaySpecs();
        if (isElbowProtect()) System.out.println(", has elbow protection"); else System.out.println("");
    }

}

class LegArmour extends Armour{

    public LegArmour(String name, float price, float weight, int size, boolean canBeUsedOnHorse) {
        super(name, price, weight, size);
        this.canBeUsedOnHorse = canBeUsedOnHorse;
    }
    
    
    
    private boolean canBeUsedOnHorse;

    public boolean canBeUsedOnHorse() {
        return canBeUsedOnHorse;
    }

    public void setCanBeUsedOnHorse(boolean canBeUsedOnHorse) {
        this.canBeUsedOnHorse = canBeUsedOnHorse;
    }

    @Override
    public void displaySpecs() {
        super.displaySpecs(); 
        if (canBeUsedOnHorse()) System.out.println(", can be used on horse"); else System.out.println("");
    }
    
    
    

}

class TorsoArmour extends Armour{

    public TorsoArmour(String name, float price, float weight, int size, boolean isBooletProof, int booletProofLevel) {
        super(name, price, weight, size);
        this.isBooletProof = isBooletProof;
        this.booletProofLevel = booletProofLevel;
    }
    
    
    private boolean isBooletProof;

    public boolean isBooletProof() {
        return isBooletProof;
    }

    public void setIsBooletProof(boolean isBooletProof) {
        this.isBooletProof = isBooletProof;
    }

    private int booletProofLevel;

    public int getBooletProofLevel() {
        return booletProofLevel;
    }

    public void setBooletProofLevel(int booletProofLevel) {
        this.booletProofLevel = booletProofLevel;
    }

    @Override
    public void displaySpecs() {
        super.displaySpecs(); //To change body of generated methods, choose Tools | Templates.
        if (isBooletProof()) System.out.println(", is booletproof, booletproof level: "+getBooletProofLevel()); else System.out.println("");
    }
    
    

}

class HeadArmour extends Armour{

    public HeadArmour(String name, float price, float weight, int size, boolean faceProtected) {
        super(name, price, weight, size);
        this.faceProtected = faceProtected;
    }
    
    
    private boolean faceProtected;

    public boolean isFaceProtected() {
        return faceProtected;
    }

    public void setFaceProtected(boolean faceProtected) {
        this.faceProtected = faceProtected;
    }

    @Override
    public void displaySpecs() {
        super.displaySpecs(); //To change body of generated methods, choose Tools | Templates.
        if (isFaceProtected()) System.out.println(", has face protection"); else System.out.println("");
    }
    
}


