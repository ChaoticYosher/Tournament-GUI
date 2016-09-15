import java.util.HashMap;

public class RankSystem {
	private static HashMap<Integer,Integer> rankingSystem = new HashMap<Integer,Integer>();
	private static int pointsKO = 1, pointsFall = -1, pointsSD = -2;
	public static void setRanking(int rank, int value){
		rankingSystem.put(rank,value);
	}
	
	public static void setKOPoints(int value){
		pointsKO = value;
	}
	
	public static void setFallPoints(int value){
		pointsFall = value;
	}
	
	public static void setSDPoints(int value){
		pointsSD = value;
	}

	public static int score(int ko, int fall, int sd, int rank){
		return pointsKO*ko + pointsFall*fall + pointsSD*sd + rankingSystem.get(rank);
	}
}
