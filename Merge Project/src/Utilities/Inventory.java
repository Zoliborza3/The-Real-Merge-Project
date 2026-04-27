package Utilities;

import java.util.LinkedList;

import Utilities.Elements.Element;

public class Inventory {
    public LinkedList<Element> elements =new LinkedList<>();
    public int size;

    public Inventory(int size) {
        this.size = size;
    }
    public int getNumberOfElements(){
        int elementAmount=0;
        for (Element i:elements){
            elementAmount++;
        }
        return elementAmount;
    }
    public double getElementAmount(){
        double itemAmount=0;
        for (Element i:elements) {
            itemAmount+=i.amount;
        }
        return itemAmount;
    }
    public void addElement(Element addedElement){
        if (getElementAmount()+1<=size) {
            boolean found=false;
            for (Element i : elements) {
                if (i.id == addedElement.id) {
                    i.amount += addedElement.amount;
                    found=true;
                    break;
                }
            }
            if (!found){
                    elements.add(addedElement);
            }
        }
    }
    public String toString(){
        String toReturn = "";

        toReturn += "elements : "+elements.toString()+", size : "+size;

        return toReturn;
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }
}
