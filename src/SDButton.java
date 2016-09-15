import processing.core.PVector;

public class SDButton extends StatButton {
	public SDButton(PVector ul, PVector lr) {
		super(ul, lr, true);
	}
	public void set( int sd ){
		if( sd < 0 ){
			stat.setSD( 0 );
		} else {
			stat.setSD(sd);
		}
	}
	public int get(){
		return stat.sd();
	}
}