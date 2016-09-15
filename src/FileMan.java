import processing.core.PApplet;
import processing.core.PImage;


public class FileMan {
	private static FileMan inst;
	private boolean linux;
	private String filePath;
	private PApplet p;
	public FileMan(PApplet p, boolean linux, String filePath){
		this.p = p;
		this.linux = linux;
		this.filePath = filePath;
		inst = this;
	}
	public static FileMan instance(){
		return inst;
	}
	public static PImage fighter(String f){
		if( inst != null ){
			return inst.p.loadImage(inst.filePath + (inst.linux?"/":"\\") + f + ".png");
		} else {
			return new PImage();
		}
	}
}
