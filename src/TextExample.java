import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;


public class TextExample extends PApplet {
	ArrayList<PFont> fonts;
	int font, n;
	boolean paused;
	public void settings(){
		size(400,300);
	}
	public void setup(){
		frameRate(1);
		textAlign(CENTER);
		n = PFont.list().length;
		fonts = new ArrayList<PFont>();
//		for( String str : PFont.list() ){
//			fonts.add( createFont(str,24) );
//		}
	}
	
	public void draw(){
		if( !paused ){
			background(0);
//			font = (font + 1) % fonts.size();
			font = (font + 1) % n;
//			textFont(fonts.get(font));
			textFont(createFont(PFont.list()[font],24));
			
			text(font+": What a cunt",width/2,height/2);
		}
	}
	
	public void mouseClicked(){
		paused = !paused;
	}
	
	public static void main( String[] args ){
		PApplet.main(new String[]{"TextExample"});;
	}
}
