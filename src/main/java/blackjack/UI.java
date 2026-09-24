package blackjack;

import java.util.Scanner;

public class UI {
    boolean askWhetherWeDeal() {
        System.out.print("Haluatko kortin? ");
        new Scanner(System.in).nextLine();

        return true;
    }
}
