import java.math.BigInteger;
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

        var part2 = part2(input);
        System.out.println("Part 2: " + part2);
    }

    private static BigInteger part2(List<String> input) {
        var cards = new BigInteger("119315717514047");
        var turns = new BigInteger("101741582076661");
        var endPos = new BigInteger("2020");
        BigInteger y = reverse(input, cards, endPos);
        BigInteger z = reverse(input, cards, y);
        BigInteger a = y.subtract(z).multiply(endPos.subtract(y).add(cards).modInverse(cards)).mod(cards);
        BigInteger b = y.subtract(a.multiply(endPos)).mod(cards);
        BigInteger modPow = a.modPow(turns, cards);
        return modPow.multiply(endPos).add(modPow.subtract(BigInteger.ONE).multiply(a.subtract(BigInteger.ONE).modInverse(cards).multiply(b))).mod(cards);
    }

    private static BigInteger reverse(List<String> input, BigInteger cards, BigInteger pos) {
        BigInteger loop = pos;
        for (int i = input.size()-1; i >= 0; --i) {
            String action = input.get(i);
            if (action.equals("deal into new stack")) {
                loop = cards.subtract(loop).subtract(BigInteger.ONE);
                if (loop.signum() == -1) {
                    loop = loop.add(cards);
                }
            } else if (action.startsWith("cut")) {
                int n = Integer.parseInt(action.substring(4));
                loop = loop.add(cards).add(new BigInteger(Integer.toString(n))).mod(cards);
            } else if (action.startsWith("deal with increment")) {
                int n = Integer.parseInt(action.substring(20));
                loop = new BigInteger(Integer.toString(n)).modInverse(cards).multiply(loop).mod(cards);
            }
        }
        return loop;
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
