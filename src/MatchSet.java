import java.util.ArrayList;
import java.util.Iterator;

public class MatchSet implements Iterable<Match>{
  ArrayList<Match> matches;
  static ArrayList<Match> master;
  int matchesPerSet, playersPerMatch;
  boolean newMatch;
  public MatchSet( int playersPerMatch, int matchesPerSet ){
	  this.matches = new ArrayList<Match>();
	  this.matchesPerSet = matchesPerSet;
	  this.playersPerMatch = playersPerMatch;
  }
  
  public static void setMatchList( ArrayList<Match> matchList ){
	  master = matchList;
  }
  
  public boolean full(){
	  return matches.size() >= matchesPerSet && matches.get(matches.size()-1).players().size() >= playersPerMatch;
  }
  
  private Match currMatch(){
	  if( matches.size() > 0 ){
		  // there are more matches to add
		  if ( matches.size() < matchesPerSet ){
			  Match m = matches.get(matches.size()-1);
			  // this match has too many players
			  if( m.players().size() >= playersPerMatch ){
				  return newMatch();
			  // more players can be added to this match
			  } else {
				  return m;
			  }
		  // last match in the set
		  } else if( matches.size() == matchesPerSet ){
			  Match m = matches.get(matches.size()-1);
			  // this match has too many players
			  if( m.players().size() >= playersPerMatch ){
				  return null;
			  // more players can be added to this match
			  } else {
				  return m;
			  }
		  } else {
			  return null;
		  }
	  // no matches are in the set yet
	  } else {
		  return newMatch();
	  }
  }
  
  private Match newMatch(){
	  Match m = new Match();
	  matches.add( m );
	  master.add( m );
	  return m;
  }
  
  public boolean isAllowed( Player p ){
	  if( full() ) return false;
	  Match m = currMatch();
	  if( m == null ) return false;
	  return !m.players().contains(p);
  }
  
  public void add( Player p ){
	Match m = currMatch();
	// add player to that match
	m.add(p);
  }

  public Match match(int i){
    return i >= 0 && i < matches.size() ? matches.get(i) : null;
  }
  
  public int status(){
    return 0;
  }

  public Iterator<Match> iterator() {
	return matches.iterator();
  }
  
  public String toString(){
	  return matches.toString();
  }
}
