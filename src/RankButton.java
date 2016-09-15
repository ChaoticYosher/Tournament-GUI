import processing.core.PVector;

public class RankButton extends StatButton {
	private int maxRank;
	public RankButton(PVector ul, PVector lr, int maxRank) {
		super(ul, lr, false);
		this.maxRank = maxRank;
	}
	public void set( int rank ){
		if( rank < 1 || rank > maxRank ){
			stat.setRank(maxRank);
		} else {
			stat.setRank(rank);
		}
	}
	public int get(){
		return stat.rank();
	}
}