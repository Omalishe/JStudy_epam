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
class Knight {
    class BodyPart<E>{
        
        private boolean isProtected;
        private String bodyPartName;
        public boolean isIsProtected() {
            return isProtected;
        }

        public void setIsProtected(boolean isProtected) {
            this.isProtected = isProtected;
        }

        private E armour;

        public E getArmour() {
            return armour;
        }

        public void setArmour(E armour) {
            this.armour = armour;
            if (armour!=null) setIsProtected(true);
            else setIsProtected(false);
        }

        public BodyPart(boolean isProtected, E armour, String bodyPartName) {
            this.isProtected = isProtected;
            this.armour = armour;
            this.bodyPartName = bodyPartName;
        }
        
        public BodyPart(String bodyPartName){
            this(false,null,bodyPartName);
        }

        public BodyPart() {
            this(false,null,"");
        }

        /**
         * @return the bodyPartName
         */
        public String getBodyPartName() {
            return bodyPartName;
        }

        /**
         * @param bodyPartName the bodyPartName to set
         */
        public void setBodyPartName(String bodyPartName) {
            this.bodyPartName = bodyPartName;
        }
        
    }
    
    BodyPart<HeadArmour> head;
    BodyPart<ArmArmour> leftArm;
    BodyPart<ArmArmour> rightArm;
    BodyPart<TorsoArmour> torso;
    BodyPart<LegArmour> leftLeg;
    BodyPart<LegArmour> rightLeg;

    public Knight(HeadArmour head, 
            ArmArmour leftArm, 
            ArmArmour rightHand, 
            TorsoArmour torso, 
            LegArmour leftLeg, 
            LegArmour rightLeg) {
        this.head = new BodyPart<>(true, head, "Head");
        this.leftArm = new BodyPart<>(true, leftArm, "Left arm");
        this.rightArm = new BodyPart<>(true, rightHand, "Right arm");
        this.torso = new BodyPart<>(true, torso, "Torso");
        this.leftLeg = new BodyPart<>(true, leftLeg, "Left leg");
        this.rightLeg = new BodyPart<>(true, rightLeg,"Right leg");
    }

    public Knight() {
        this.head = new BodyPart<>("Head");
        this.leftArm = new BodyPart<>("Left arm");
        this.rightArm = new BodyPart<>("Right arm");
        this.torso = new BodyPart<>("Torso");
        this.leftLeg = new BodyPart<>("Left leg");
        this.rightLeg = new BodyPart<>("Right leg");
    }
    
    public double getTotalArmourWeight(){
        double acc=0.0;
        if (head.isProtected) acc+=head.getArmour().getWeight();
        if (leftArm.isProtected) acc+=leftArm.getArmour().getWeight();
        if (rightArm.isProtected) acc+=rightArm.getArmour().getWeight();
        if (torso.isProtected) acc+=torso.getArmour().getWeight();
        if (leftLeg.isProtected) acc+=leftLeg.getArmour().getWeight();
        if (rightLeg.isProtected) acc+=rightLeg.getArmour().getWeight();
        return acc;
    }
    
    public double getTotalArmourCost(){
        double acc=0.0;
        if (head.isProtected) acc+=head.getArmour().getPrice();
        if (leftArm.isProtected) acc+=leftArm.getArmour().getPrice();
        if (rightArm.isProtected) acc+=rightArm.getArmour().getPrice();
        if (torso.isProtected) acc+=torso.getArmour().getPrice();
        if (leftLeg.isProtected) acc+=leftLeg.getArmour().getPrice();
        if (rightLeg.isProtected) acc+=rightLeg.getArmour().getPrice();
        return acc;
    }
            
}
