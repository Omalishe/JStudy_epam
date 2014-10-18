/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework1_task1;

/**
 *
 * @author oleksandr
 */

public class HomeWork1_task1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Shape Ring = new Shape(10,"Ring");
        Shape Triangle = new Shape(20,"Triangle");
        Shape Parallelogram = new Shape(80,"Parallelogram");
        Shape Trapezium = new Shape(90,"Trapezium");
        
        
        Shape complexShape = Shape.sumShapes(Triangle, Ring, "complexShape");
        
        System.out.println("The area of "+Ring.getShapeName()+" is "+Ring.getArea());
        System.out.println("The area of "+Triangle.getShapeName()+" is "+Triangle.getArea());
        System.out.println("----------------------------------------------------------");
        System.out.println("The area of "+complexShape.getShapeName()+" is "+complexShape.getArea());
    }
    
}
