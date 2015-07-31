import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @author costelradulescu
 *
 */
public class Application {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Game game = new Game();
    game.start();
  }

  static class Game {
    char winner = '.';
    Board b;

    void start() {
      b = new Board();
      boolean xsTurn = true;
      Scanner scanner = new Scanner(System.in);
      while (!isOver(b.getCombos())) {
        System.out.println(b);
        System.out.println("Enter space separated coordinates for " + (xsTurn ? "X" : "O"));
        int input[] = getCoordinates(scanner);
        int i = input[0];
        int j = input[1];
        if (xsTurn) {
          try {
            b.setX(i, j);
          } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            continue;
          } catch (IllegalStateException ise) {
            System.err.println(ise.getMessage());
            continue;
          }
          xsTurn = false;
        } else {
          try {
            b.setO(i, j);
          } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            continue;
          } catch (IllegalStateException ise) {
            System.err.println(ise.getMessage());
            continue;
          }
          xsTurn = true;
        }
      }
      scanner.close();
      System.out.println(b);
      if (winner == '.') {
        System.out.println("DRAW!");
      } else {
        System.out.println("The winner is " + winner);
      }

    }

    private int[] getCoordinates(Scanner scanner) {
      boolean correct = false;
      int[] coordinates = new int[2];
      while (!correct) {
        String line = scanner.nextLine();
        try {
          StringTokenizer tokenizer = new StringTokenizer(line);
          if (tokenizer.hasMoreTokens()) {
            coordinates[0] = Integer.parseInt(tokenizer.nextToken());
          }
          if (tokenizer.hasMoreTokens()) {
            coordinates[1] = Integer.parseInt(tokenizer.nextToken());
          }
          correct = true;
        } catch (NumberFormatException nfe) {
          System.err.println("Please enters valid numbers for coordinates (ie: 1 1)");
        }
      }
      return coordinates;
    }

    boolean isOver(List<Combo> combos) {
      boolean over = true;
      for (Combo combo : combos) {
        if (combo.isWinning()) {
          winner = combo.line.charAt(0);
          return true;
        }
        over &= combo.isComplete();
      }
      return over;
    }
  }

  static class Combo {
    String line;

    Combo(String s) {
      line = s;
    }

    boolean isComplete() {
      return !line.contains(".");
    }

    boolean isWinning() {
      if (!isComplete())
        return false;
      return line.charAt(0) == line.charAt(1) && line.charAt(1) == line.charAt(2);
    }

    public String toString() {
      return line;
    }
  }

  static class Board {
    static final int SIZE = 3;
    String[] board = new String[SIZE];

    Board() {
      reset();
    }

    public void reset() {
      for (int i = 0; i < SIZE; i++) {
        board[i] = new String("...");
      }
    }

    public void setO(int i, int j) {
      set(i, j, 'O');
    }

    public void setX(int i, int j) {
      set(i, j, 'X');
    }

    public List<Combo> getCombos() {
      List<Combo> combos = new ArrayList<Combo>();
      for (int i = 0; i < SIZE; i++) {
        combos.add(new Combo(board[i]));
        combos.add(new Combo(String.valueOf(board[0].charAt(i)) + String.valueOf(board[1].charAt(i))
            + String.valueOf(board[2].charAt(i))));
      }
      combos.add(new Combo(String.valueOf(board[0].charAt(0)) + String.valueOf(board[1].charAt(1))
          + String.valueOf(board[2].charAt(2))));
      combos.add(new Combo(String.valueOf(board[0].charAt(2)) + String.valueOf(board[1].charAt(1))
          + String.valueOf(board[2].charAt(0))));
      return combos;
    }

    private void set(int i, int j, char c) {
      if (i < 0 || i > 2 || j < 0 || j > 2)
        throw new IllegalArgumentException("i and j must be between 0 and 2 inclusive");
      char[] newLine = board[i].toCharArray();
      if (newLine[j] != '.')
        throw new IllegalStateException("can only set an empty cell");
      newLine[j] = c;
      board[i] = new String(newLine);
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("+-+-+-+");
      sb.append("\n");
      for (int i = 0; i < SIZE; i++) {
        sb.append("|");
        for (int j = 0; j < SIZE; j++) {
          sb.append(board[i].charAt(j));
          sb.append("|");
        }
        sb.append("\n");
        sb.append("+-+-+-+");
        sb.append("\n");
      }
      return sb.toString();
    }

  }

}
