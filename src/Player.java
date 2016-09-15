import java.util.Random;

public class Player implements Comparable<Player>{
  String name, fighter;
  PlayerStat stat;
  static Random gen = new Random();
  public Player(String name, String fighter){
    this.name = name;
    this.fighter = fighter;
    stat = new PlayerStat();
  }
  public String name(){
	return name;
  }
  public String fighter(){
	return fighter;
  }
  public void addStat(MatchStat s){
	  stat.add(s);
  }
  public void removeStat(MatchStat s){
	  stat.remove(s);
  }
  public PlayerStat stat(){
	  return stat;
  }
  public int compareTo( Player a ){
	  return a.stat.score() - stat.score();
  }
  public String toString(){
	  return "[" + name + " : " + fighter + "]";
  }  
}