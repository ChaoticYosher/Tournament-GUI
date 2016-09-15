import processing.core.PVector;

public class KOButton extends StatButton {
	public KOButton(PVector ul, PVector lr) {
		super(ul, lr, true);
	}
	public void set( int ko ){
		if( ko < 0 ){
			stat.setKO(0);
		} else {
			stat.setKO(ko);
		}
	}
	public int get(){
		return stat.ko();
	}
}