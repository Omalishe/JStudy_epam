package project1_knight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Project1_Knight {

    static Armour[] availableArmour;
    static Knight knight;
    
    static String[] mainMenu;
    static String[] showArmourMenu;
    static String[] manageArmourOnKnightMenu;
    static String[] knightBodyPartsMenu;
    static String[] knightBodyPartsAllMenu;

    private static void manageAvailableArmour() {
        byte selection;
        Scanner in = new Scanner(System.in);
        while (true) {
            displayMenu(showArmourMenu);
            
            if (in.hasNextByte()) selection = in.nextByte(); else {
                selection = -1;
                in.next();
            }
            
            switch(selection){
                case 1: showArmour("All"); break;
                case 2: showArmour("AllSorted"); break;
                case 3: showArmour("PriceRange"); break;
                case 0: return;
            }
        }
    }

    private static void manageArmourOnKnight() {
        byte selection;
        Scanner in = new Scanner(System.in);
        while (true) {
            displayMenu(manageArmourOnKnightMenu);
            
            if (in.hasNextByte()) selection = in.nextByte(); else {
                selection = -1;
                in.next();
            }
            
            switch(selection){
                case 1: getArmourOnKnightMenu(); break;
                case 2: getArmourOffKnightMenu(); break;
                case 3: System.out.println("\nTotal cost of armour on knight: "+knight.getTotalArmourCost()+"\n"); break;
                case 4: System.out.println("\nTotal weight of armour on knight: "+knight.getTotalArmourWeight()+"\n"); break;
                case 5: showAllArmourOnKnight(); break;
                case 0: return;
            }
        }
    }

    private static void showArmour(String showType) {
        switch(showType){
            case "All": 
                for (Armour a:availableArmour){
                    a.displaySpecs();
                }
            break;
            case "AllSorted": 
                Armour[] aa = Arrays.copyOf(availableArmour, availableArmour.length);
                Arrays.sort(aa);
                for (Armour a:aa){
                    a.displaySpecs();
                }
            break;
            case "PriceRange": 
                double lowPrice=0.0;
                double highPrice=0.0;
                boolean entered=false;
                Scanner in = new Scanner(System.in);
                
                do{
                    System.out.print("Enter low price bound: ");
                    if (in.hasNextDouble()){
                        lowPrice = in.nextDouble();
                        entered=true;                     
                    }else in.next();
                }while(!entered);
                
                entered=false;
                
                do {
                    System.out.print("Entere high price bound: ");
                    if (in.hasNextDouble()){
                        highPrice = in.nextDouble();
                        entered = true;
                    }else in.next();
                }while(!entered);
                
                List<Armour> selectedElements = new ArrayList<>();
                for (Armour a:availableArmour){
                    if (a.getPrice()>=lowPrice && a.getPrice()<=highPrice){
                        selectedElements.add(a);
                    }
                }
                
        for (Armour a : selectedElements) {
            a.displaySpecs();
        }
                
                
        }
    }

    private static void getArmourOnKnightMenu() {
        byte selection;
        Scanner in = new Scanner(System.in);
        while (true) {
            displayMenu(knightBodyPartsMenu);
                        
            if (in.hasNextByte()) selection = in.nextByte(); else {
                selection = -1;
                in.next();
            }
            
            switch(selection){
                case 1: getArmourOnKnight(knight.head,HeadArmour.class); break;
                case 2: getArmourOnKnight(knight.leftArm, ArmArmour.class); break;
                case 3: getArmourOnKnight(knight.rightArm, ArmArmour.class); break;
                case 4: getArmourOnKnight(knight.torso, TorsoArmour.class); break;
                case 5: getArmourOnKnight(knight.leftLeg, LegArmour.class); break;
                case 6: getArmourOnKnight(knight.rightLeg, LegArmour.class); break;
                case 0: return;

            }
        }
    }

    private static void getArmourOnKnight(Knight.BodyPart bodypart, Class aClass) {
        if (bodypart.isIsProtected()){
            System.out.println("\nThis part of body is already armoured\n");
            return;
        }
        
        List<Armour> selectedItems=new ArrayList<>();
        int n=0;
        for (Armour a:availableArmour){
            
            if(a.getClass().equals(aClass)){
                selectedItems.add(a);
            }
        }
        
        boolean selected = false;
        Scanner in = new Scanner(System.in);
        byte selection;
        Armour selectedValue=null;
        
        do{
            for (Armour a:selectedItems){
                int index = selectedItems.indexOf(a);
                System.out.println(""+index+". "+a.getName());
            }
            if (in.hasNextByte()){
                selection = in.nextByte();
                
                try{
                    selectedValue = selectedItems.get(selection);
                    selected=true;
                }catch (IndexOutOfBoundsException e){
                    selected=false;
                }
            }else in.next();
        }while(!selected);
        
        
        bodypart.setArmour(selectedValue);
        bodypart.setIsProtected(true);
        System.out.println("\n"+bodypart.getBodyPartName()+" is now armoured!\n");
        
        
    }

    private static void getArmourOffKnightMenu() {
        byte selection;
        Scanner in = new Scanner(System.in);
        while (true) {
            displayMenu(knightBodyPartsAllMenu);
                        
            if (in.hasNextByte()) selection = in.nextByte(); else {
                selection = -1;
                in.next();
            }
            
            switch(selection){
                case 1: getArmourOffKnight(knight.head); break;
                case 2: getArmourOffKnight(knight.leftArm); break;
                case 3: getArmourOffKnight(knight.rightArm); break;
                case 4: getArmourOffKnight(knight.torso); break;
                case 5: getArmourOffKnight(knight.leftLeg); break;
                case 6: getArmourOffKnight(knight.rightLeg); break;
                case 7: getAllArmourOffKnight(); break;
                case 0: return;

            }
        }
    }

    private static void getArmourOffKnight(Knight.BodyPart bodyPart) {
        bodyPart.setArmour(null);
        bodyPart.setIsProtected(false);
        System.out.println("\nThe Knight's "+bodyPart.getBodyPartName()+" is unprotected\n");
    }
    
    private static void getAllArmourOffKnight() {
        knight.head.setArmour(null);
        knight.head.setIsProtected(false);

        knight.leftArm.setArmour(null);
        knight.leftArm.setIsProtected(false);

        knight.rightArm.setArmour(null);
        knight.rightArm.setIsProtected(false);

        knight.torso.setArmour(null);
        knight.torso.setIsProtected(false);

        knight.leftLeg.setArmour(null);
        knight.leftLeg.setIsProtected(false);

        knight.rightLeg.setArmour(null);
        knight.rightLeg.setIsProtected(false);

        System.out.println("\nThe Knight is totally unprotected!\n");
    }

    private static void displayMenu(String[] mainMenu, String...additionalEntrys) {
        System.out.println("");
        for (String s:mainMenu){
            System.out.println(s);
        }
        for (String s:additionalEntrys){
            System.out.println(s);
        }
    }

    private static void showAllArmourOnKnight() {
        if(knight.head.isIsProtected()) {
            System.out.print(knight.head.getBodyPartName()+": ");
            knight.head.getArmour().displaySpecs();
        }
        
        if(knight.leftArm.isIsProtected()) {
            System.out.print(knight.leftArm.getBodyPartName()+": ");
            knight.leftArm.getArmour().displaySpecs();
        }
        
        if(knight.rightArm.isIsProtected()) {
            System.out.print(knight.rightArm.getBodyPartName()+": ");
            knight.rightArm.getArmour().displaySpecs();
        }
        
        if(knight.torso.isIsProtected()) {
            System.out.print(knight.torso.getBodyPartName()+": ");
            knight.torso.getArmour().displaySpecs();
        }
        
        if(knight.leftLeg.isIsProtected()) {
            System.out.print(knight.leftLeg.getBodyPartName()+": ");
            knight.leftLeg.getArmour().displaySpecs();
        }
        
        if(knight.rightLeg.isIsProtected()) {
            System.out.print(knight.rightLeg.getBodyPartName()+": ");
            knight.rightLeg.getArmour().displaySpecs();
        }
        
    }

    public Project1_Knight() {
        showArmourMenu = new String[5];
        showArmourMenu[0]="----Show available armour----";
        showArmourMenu[1]="1. All";
        showArmourMenu[2]="2. All (sorted by weight)";
        showArmourMenu[3]="3. Range by price";
        showArmourMenu[4]="0. Go back";
        
        manageArmourOnKnightMenu = new String[7];
        manageArmourOnKnightMenu[0]="----Manage armour on Knight----";
        manageArmourOnKnightMenu[1]="1. Get armour on Knight";
        manageArmourOnKnightMenu[2]="2. Get armour off Knight";
        manageArmourOnKnightMenu[3]="3. Show total cost";
        manageArmourOnKnightMenu[4]="4. Show total weight";
        manageArmourOnKnightMenu[5]="5. Show all armour on Knight";
        manageArmourOnKnightMenu[6]="0. Go back";
        
        knightBodyPartsMenu = new String[6];
        knightBodyPartsMenu[0]="----Get armour on knight----";
        knightBodyPartsMenu[1]="1. Head";
        knightBodyPartsMenu[2]="2. Left hand, 3. Right hand";
        knightBodyPartsMenu[3]="4. Torso";
        knightBodyPartsMenu[4]="5. Left leg, 6. right leg";
        knightBodyPartsMenu[5]="0. Go back";
        
        knightBodyPartsAllMenu = new String[7];
        knightBodyPartsAllMenu[0]="----Get armour on knight----";
        knightBodyPartsAllMenu[1]="1. Head";
        knightBodyPartsAllMenu[2]="2. Left hand, 3. Right hand";
        knightBodyPartsAllMenu[3]="4. Torso";
        knightBodyPartsAllMenu[4]="5. Left leg, 6. right leg";
        knightBodyPartsAllMenu[5]="7. All";
        knightBodyPartsAllMenu[6]="0. Go back";
        
        
        mainMenu = new String[4];
        mainMenu[0]="----Main menu----";
        mainMenu[1]="1. Show available armour";
        mainMenu[2]="2. Manage armour on knight";
        mainMenu[3]="0. Exit";
        
        availableArmour = new Armour[12];
        
        
        availableArmour[0]=new HeadArmour("Heavy helmet", 600.2f, 1.0f, 48, true);
        availableArmour[1]=new HeadArmour("Light helmet", 80.9f, 0.5f, 48, false);
        availableArmour[2]=new HeadArmour("Carbon helmet", 800, 0.5f, 48, true);
        
        availableArmour[3]=new TorsoArmour("Leather jacket", 50.0f, 1.5f, 48, false, 0);
        availableArmour[4]=new TorsoArmour("Steel jacket", 250.0f, 15f, 48, true, 15);
        availableArmour[5]=new TorsoArmour("Carbon body shields", 500.0f, 2f, 48, true, 8);
        
        availableArmour[6]=new ArmArmour("Leather sleeve", 12, 0.2f, 48, false);
        availableArmour[7]=new ArmArmour("Steel sleeve", 60, 5.5f, 48, true);
        availableArmour[8]=new ArmArmour("Carbon sleeve", 80, 0.3f, 48, true);
        
        availableArmour[9]=new LegArmour("Leather leg shield", 12, 0.4f, 48, true);
        availableArmour[10]=new LegArmour("Steel leg shield", 60, 8.4f, 48, false);
        availableArmour[11]=new LegArmour("Carbon leg shield", 80, 0.5f, 48, true);
        
        knight = new Knight(
                (HeadArmour)availableArmour[1]
                , (ArmArmour)availableArmour[7]
                , (ArmArmour)availableArmour[7]
                , (TorsoArmour)availableArmour[3]
                , (LegArmour)availableArmour[9]
                , (LegArmour)availableArmour[9]);
        //knigth = new Knight();
        
    }
    
    public static void main(String[] args) {
        Project1_Knight project1_Knight = new Project1_Knight();
        byte selection;
        Scanner in = new Scanner(System.in);
        while (true) {
            displayMenu(mainMenu);
            
            if (in.hasNextByte()) selection = in.nextByte(); else {
                selection = -1;
                in.next();
            }
            switch(selection){
                case 1: manageAvailableArmour(); break;
                case 2: manageArmourOnKnight(); break;
                case 0: System.exit(0);

            }
        }
        
    }
    
    
    
}
