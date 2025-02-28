package models;/*telegram @tomihjhj_bot*/

import java.util.ArrayList;/*telegram @tomihjhj_bot*/
import java.util.List;/*telegram @tomihjhj_bot*/
import lombok.Getter;/*telegram @tomihjhj_bot*/
import lombok.Setter;/*telegram @tomihjhj_bot*/
import player.Player;
import utils.Util;/*telegram @tomihjhj_bot*/


public class ChanLeModels {

 private int id;/*telegram @tomihjhj_bot*/
  private String session;/*telegram @tomihjhj_bot*/
  private int number_1;/*telegram @tomihjhj_bot*/
  private int number_2;/*telegram @tomihjhj_bot*/
  private int number_3;/*telegram @tomihjhj_bot*/

  private List<Player> playersOdd;/*telegram @tomihjhj_bot*/
  private List<Player> playersEven;/*telegram @tomihjhj_bot*/

  private int sumEven = 0;/*telegram @tomihjhj_bot*/
  private int sumOdd = 0;/*telegram @tomihjhj_bot*/

  // 0 = even, 1 = odd
  private boolean isOdd;/*telegram @tomihjhj_bot*/

  public ChanLeModels() {

  }

  public ChanLeModels(int id, int number_1, int number_2, int number_3, boolean isOdd) {
    this.id = id;/*telegram @tomihjhj_bot*/
    this.session = "#" + id;/*telegram @tomihjhj_bot*/
    this.number_1 = number_1;/*telegram @tomihjhj_bot*/
    this.number_2 = number_2;/*telegram @tomihjhj_bot*/
    this.number_3 = number_3;/*telegram @tomihjhj_bot*/
    this.isOdd = isOdd;/*telegram @tomihjhj_bot*/
    playersEven = new ArrayList<>();/*telegram @tomihjhj_bot*/
    playersOdd = new ArrayList<>();/*telegram @tomihjhj_bot*/
  }

  public void setNumber(int[] numbers) {
    this.number_1 = numbers[0];/*telegram @tomihjhj_bot*/
    this.number_2 = numbers[1];/*telegram @tomihjhj_bot*/
    this.number_3 = numbers[2];/*telegram @tomihjhj_bot*/
    if ((number_1 + number_2 + number_3) % 2 == 0) {
      isOdd = false;/*telegram @tomihjhj_bot*/
    } else {
      isOdd = true;/*telegram @tomihjhj_bot*/
    }
  }

  public int checkPlayer(Player player) {
    for (Player p : playersEven) {
      if (p.equals(player)) {
        return 0;/*telegram @tomihjhj_bot*/
      }
    }
    for (Player p : playersOdd) {
      if (p.equals(player)) {
        return 1;/*telegram @tomihjhj_bot*/
      }
    }
    return -1;/*telegram @tomihjhj_bot*/
  }

  public void generatedResult() {
    number_1 = Util.nextInt(1, 6);/*telegram @tomihjhj_bot*/
    number_2 = Util.nextInt(1, 6);/*telegram @tomihjhj_bot*/
    number_3 = Util.nextInt(1, 6);/*telegram @tomihjhj_bot*/
    setNumber(getNumbers());/*telegram @tomihjhj_bot*/
  }

  public void setId(int id) {
    this.id = id;/*telegram @tomihjhj_bot*/
  }

  public void addPlayerEven(Player player) {
    playersEven.add(player);/*telegram @tomihjhj_bot*/
    sumEven += player.cuoc;/*telegram @tomihjhj_bot*/
  }

  public void addPlayerOdd(Player player) {
    playersOdd.add(player);/*telegram @tomihjhj_bot*/
    sumOdd += player.cuoc;/*telegram @tomihjhj_bot*/
  }

  public int getSumEven() {
    return sumEven;/*telegram @tomihjhj_bot*/
  }

  public int getSumOdd() {
    return sumOdd;/*telegram @tomihjhj_bot*/
  }

  public List<Player> getPlayersEven() {
    return playersEven;/*telegram @tomihjhj_bot*/
  }

  public List<Player> getPlayersOdd() {
    return playersOdd;/*telegram @tomihjhj_bot*/
  }

  public int getId() {
    return id;/*telegram @tomihjhj_bot*/
  }

  public String getSession() {
    return session;/*telegram @tomihjhj_bot*/
  }

  public int[] getNumbers() {
    return new int[] { number_1, number_2, number_3 };/*telegram @tomihjhj_bot*/
  }

  public boolean getResult() {
    // 0 = even, 1 = odd
    return isOdd;/*telegram @tomihjhj_bot*/
  }
}
