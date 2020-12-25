import java.util.List;

public class Day22 {

    public static void main(String[] args) {
        var input = Util.readStrings();
        var cards = new int[10007];
        for (int i = 0; i < cards.length; ++i) {
            cards[i] = i;
        }

        var part1 = part1(cards, input);
        System.out.println("Part 1: " + part1);
    }

    private static int part1(int[] cards, List<String> input) {
        for (String action : input) {
            int[] newCards = new int[cards.length];
            if (action.equals("deal into new stack")) {
                for (int i = 0; i < cards.length; ++i) {
                    newCards[i] = cards[cards.length-i-1];
                }
            } else if (action.startsWith("cut")) {
                int n = Integer.parseInt(action.substring(4));
                if (n > 0) {
                    for (int i = n; i < cards.length; ++i) {
                        newCards[i-n] = cards[i];
                    }
                    for (int i = 0; i < n; ++i) {
                        newCards[cards.length-n+i] = cards[i];
                    }
                } else if (n < 0) {
                    n = Math.abs(n);
                    for (int i = 0; i < cards.length-n; ++i) {
                        newCards[n+i] = cards[i];
                    }
                    for (int i = 0; i < n; ++i) {
                        newCards[i] = cards[cards.length-n+i];
                    }
                }
            } else if (action.startsWith("deal with increment")) {
                int n = Integer.parseInt(action.substring(20));
                for (int i = 0; i < cards.length; ++i) {
                    newCards[(i*n)%newCards.length] = cards[i];
                }
            }
            cards = newCards;
        }
        for (int i = 0; i < cards.length; ++i) {
            if (cards[i] == 2019) {
                return i;
            }
        }
        throw new RuntimeException("Could not find card 2019");
    }
}
