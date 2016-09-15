import processing.core.PVector;

public class FallButton extends StatButton {
	public FallButton(PVector ul, PVector lr) {
		super(ul, lr, true);
	}
	public void set( int fall ){
		if( fall < 0 ){
			stat.setFall( 0 );
		} else {
			stat.setFall(fall);
		}
	}
	public int get(){
		return stat.fall();
	}
}