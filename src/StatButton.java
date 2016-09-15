import processing.core.PVector;

public abstract class StatButton {
	PVector ul, lr;
	MatchStat stat;
	boolean vert;
	public StatButton(PVector ul, PVector lr, boolean vert){
		this.ul = new PVector( ul.x, ul.y );
		this.lr = new PVector( lr.x, lr.y );
		this.vert = vert;
	}
	public void click( int mx, int my ){
	  if( mx > ul.x && mx < lr.x && my > ul.y && my < lr.y ){
		if( vert ){
		  if( mx < (ul.x + lr.x)/2 ){
			set(get()-1);
		  } else {
			set(get()+1);
	  	  }
		} else {
		  if( mx < (ul.x + lr.x)/2 ){
			set(get()+1);
		  } else {
			set(get()-1);
		  }
		}
	  }
	}
	public PVector ul(){
	  return new PVector(ul.x,ul.y);
	}
	public PVector size(){
	  return new PVector(lr.x-ul.x,lr.y-ul.y);
	}
	public void setStat(MatchStat stat){
		this.stat = stat;
	}
	public abstract void set(int v);
	public abstract int get();
}
