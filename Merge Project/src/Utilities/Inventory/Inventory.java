package Utilities.Inventory;

import java.util.LinkedList;

public class Inventory {
    public static LinkedList<Element> inverntory =new LinkedList<>();
    public int size;

    public Inventory(int y) {
        size = y;
    }
    public static int getNumberOfElements(LinkedList<Element> inverntory){
        int elementAmount=0;
        for (Element i:inverntory){
            elementAmount++;
        }
        return elementAmount;
    }
    public static double getElementAmount(LinkedList<Element> inverntory){
        double itemAmount=0;
        for (Element i:inverntory) {
            itemAmount+=i.amount;
        }
        return itemAmount;
    }
    public static void addElement(Element addedElement,LinkedList<Element> inverntory,int size){
        if (getElementAmount(inverntory)+1>size){
            System.out.println("Cant add element because inventory is full");
        }
        else {
            boolean found=false;
            for (Element i : inverntory) {
                if (i.id == addedElement.id) {
                    i.amount += addedElement.amount;
                    System.out.println(addedElement.id + " Element amount increased");
                    found=true;
                    break;
                }
            }
            if (!found){
                    inverntory.add(addedElement);
                    System.out.println(addedElement.id + " Element added");
            }
        }
    }
    public static void printInventory(LinkedList<Element> inverntory){
        for (Element i:inverntory){
            System.out.println(i.id+"-"+i.amount);
        }
    }
}
