package Test;

/**
 *
 * @author Hasindu Gunathilaka
 */
public class java {

    public static void main(String[] args) {
        int qty = 2500;
        int value = (int) qty / 750;
        int balance = (int) qty - ((value) * 750);
        String inHand = String.valueOf(value + "." + balance);
        System.out.println(inHand);
    }
}
