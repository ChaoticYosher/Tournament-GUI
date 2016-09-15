public class MatchStat implements Comparable<MatchStat> {
	private int ko, fall, sd, rank;
	public MatchStat(int ko, int fall, int sd, int rank){
		this.setKO(ko);
		this.setFall(fall);
		this.setSD(sd);
		this.setRank(rank);
	}
	
	public int ko() {
		return ko;
	}
	public void setKO(int ko) {
		this.ko = ko;
	}
	public int fall() {
		return fall;
	}
	public void setFall(int fall) {
		this.fall = fall;
	}
	public int sd() {
		return sd;
	}
	public void setSD(int sd) {
		this.sd = sd;
	}
	
	public int rank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public static MatchStat zero() {
		return new MatchStat(0,0,0,0);
	}
	
	public String toString(){
		return "KO: " + ko + ", Fall: " + fall + ", SD: " + sd + ", Rank: " + rank;
	}
	
	public int score(){
		return RankSystem.score(ko,fall,sd,rank);
	}

	public int compareTo(MatchStat s){
		return s.score() - score();
	}
}
