import java.util.ArrayList;

public class PlayerStat implements Comparable<PlayerStat>{
	ArrayList<MatchStat> stat;
	PlayerStat(){
		stat = new ArrayList<MatchStat>();
	}
	public void add(MatchStat m){
		stat.add(m);
	}
	public void remove(MatchStat m){
		stat.remove(m);
	}
	public int kos(){
		int kos = 0;
		for( MatchStat m : stat ){
			kos += m.ko();
		}
		return kos;
	}
	public int falls(){
		int falls = 0;
		for( MatchStat m : stat ){
			falls += m.fall();
		}
		return falls;
	}
	public int sds(){
		int sds = 0;
		for( MatchStat m : stat ){
			sds += m.sd();
		}
		return sds;
	}
	public ArrayList<Integer> ranks(){
		ArrayList<Integer> ranks = new ArrayList<Integer>();
		int r;
		for( MatchStat m : stat ){
			r = m.rank();
			if( r > 0 ){
				ranks.add(r);
			}
		}
		return ranks;
	}
	public int score(){
		int score = 0;
		for( MatchStat m : stat ){
			score += m.score();
		}
		return score;
	}

	public int compareTo(PlayerStat a) {
		return a.score() - score();
	}
}
