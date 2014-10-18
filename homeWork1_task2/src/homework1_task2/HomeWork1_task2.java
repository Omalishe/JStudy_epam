/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
Реализация задачи по варианту 8:
8. Создать объект класса  Круг, используя классы Точка, Окружность. 
Методы: задание размеров, изменение радиуса, 
определение принадлежности точки данному кругу.
*/
package homework1_task2;

/**
 *
 * @author oleksandr
 */
public class HomeWork1_task2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Round rd = new Round(5,10,25);
        rd.moveTo(50, 20);
        Dot randomDot = new Dot(10,10);
        String substr=rd.dotIsInRound(randomDot)?"":"not ";
        String outString = "The dot with coords ("+randomDot.getX()+","+randomDot.getY()+") does "
                +substr
                +"belong to circle ("+rd.getX()+","+rd.getY()+","+rd.getRadius()+")";
        System.out.println(outString);
    }
    
}
