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
public class Shape {
    private int area;
    private String shapeName;
    
    public static Shape sumShapes(Shape firstShape, Shape secondShape, String newShapeName){
        int totalArea = firstShape.getArea() + secondShape.getArea();
        return new Shape(totalArea,newShapeName);
    }
    
    public Shape(int area, String shapeName){
        this.area=area;
        this.shapeName = shapeName;
    }

    /**
     * @return the area
     */
    public int getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(int area) {
        this.area = area;
    }

    /**
     * @return the shapeName
     */
    public String getShapeName() {
        return shapeName;
    }
}
