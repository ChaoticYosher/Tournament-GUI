import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Match implements Iterable<Player> {
  private HashMap<Player,MatchStat> stat;
  private ArrayList<Player> p;
  public Match(){
	this.p = new ArrayList<Player>();
    this.stat = new HashMap<Player,MatchStat>();
  }
  public void add( Player p ){
	MatchStat s = MatchStat.zero();
	p.addStat(s);
	this.p.add(p);
    stat.put(p,s);
  }
  public MatchStat stat( Player p ){
	  return stat.get(p);
  }
  public void swap(Match newMatch, Player oldPlayer, Player newPlayer){
	// make sure the new destination won't lead to the player's stats getting lost
	if( stat.containsKey(oldPlayer) && newMatch.stat.containsKey(newPlayer) && ( (!stat.containsKey(newPlayer) && !newMatch.stat.containsKey(oldPlayer)) || oldPlayer == newPlayer ) ){
	  // get stat from the original player
	  MatchStat oldStat = stat.get( oldPlayer );
	  // remove the original player from the hash table for the match
	  p.remove( oldPlayer );
	  // remove the original player from the player list for the match
	  stat.remove( oldPlayer );
	  // remove this match from the original player's stats
	  oldPlayer.removeStat( oldStat );
	  // get stat from the other player
	  MatchStat newStat = newMatch.stat.get( newPlayer );
	  // remove the other player from the other match hash table
	  newMatch.p.remove( newPlayer );
	  // remove the other player from the other match's player list
	  newMatch.stat.remove( newPlayer );
	  // remove the other match from the other player's stats
	  newPlayer.removeStat( newStat );
	  // move the new player into this match
	  p.add( newPlayer );
	  stat.put(newPlayer, newStat);
	  // move the old player into the other match
	  newMatch.p.add( oldPlayer );
	  newMatch.stat.put(oldPlayer, oldStat);
	  // move the old match stat into the new player
	  newPlayer.addStat( oldStat );
	  // move the new match stat into the old player
	  oldPlayer.addStat( newStat );
    }
  }
  public int ko(Player p){
	  MatchStat s = stat.get(p);
	  return s == null ? 0 : s.ko();
  }
  public int fall(Player p){
	  MatchStat s = stat.get(p);
	  return s == null ? 0 : s.fall();
  }
  public int sd(Player p){
	  MatchStat s = stat.get(p);
	  return s == null ? 0 : s.sd();
  }
  public int rank(Player p){
	  MatchStat s = stat.get(p);
	  return s == null ? 0 : s.rank();
  }
  public int score(Player p){
	  MatchStat s = stat.get(p);
	  return s == null ? 0 : s.score();
  }
  public void setKO( Player p, int ko ){
	  MatchStat s = stat.get(p);
	  if( s != null ){
		  s.setKO( ko );
	  }
  }
  public void setFall( Player p, int fall ){
	  MatchStat s = stat.get(p);
	  if( s != null ){
		  s.setFall( fall );
	  }
  }
  public void setSD( Player p, int sd ){
	  MatchStat s = stat.get(p);
	  if( s != null ){
		  s.setSD( sd );
	  }
  }
  public void setRank( Player p, int rank ){
	  MatchStat s = stat.get(p);
	  if( s != null ){
		  s.setRank( rank );
	  }
  }
  public Iterator<Player> iterator(){
	  return p.iterator();
  }
  public ArrayList<Player> players(){
	  return p;
  }
  public String toString(){
	  return stat.toString()+"\n";
  }
}
