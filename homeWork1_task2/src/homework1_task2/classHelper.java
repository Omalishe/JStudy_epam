/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.!
 Maeefwefw
 */
package homework1_task2;

/**
 *
 * @author oleksandr
 */
class Dot {
    private int x;
    private int y;
    public Dot(int x,int y){
        this.x=x;
        this.y=y;
    }
    
    public void moveTo(int x, int y){
        this.setX(x);
        this.setY(y);
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

}

class Circle{
    private double radius;
    public Circle(double radius){
        this.radius=radius;
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }
}

class Round extends Dot{
    private Circle circle;
    public Round(int x, int y, double r){
        super(x,y);
        this.circle = new Circle(r);
    }
    
    public Boolean dotIsInRound(int x, int y){
        int xDimension = x-this.getX();
        int yDimension = y-this.getY();
        double localRad = Math.sqrt(Math.pow(xDimension, 2)+Math.pow(xDimension, 2));
        return localRad<=this.circle.getRadius();
    }
    
    public Boolean dotIsInRound(Dot dot){
        return dotIsInRound(dot.getX(), dot.getY());
    }
    
    public double getRadius(){
        return this.circle.getRadius();
    }
    
}
