import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.XML;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

public class SmashApp extends PApplet {
	public void settings(){
	  //size(1164, 640);
		fullScreen();
	}
	// constants used for sizing the window
	final int PLAYER_WIDTH = 180;
	final int PLAYER_HEIGHT = 150;
	final int ADD_PLAYER_HEIGHT = 50;
	final int MATCH_PLAYER_WIDTH = 150;
	final int MATCH_HEIGHT = 160;
	
	final int SIDEBAR_WIDTH_PER_MATCH = 16;
	final int SIDEBAR_PADDING = 4;
	final int SIDEBAR_BOX_WIDTH = 2*SIDEBAR_PADDING+SIDEBAR_WIDTH_PER_MATCH;
	
	final int STANDING_FIGHTER_WIDTH = 50;
	final int STANDING_FIGHTER_HEIGHT = 60;
	final int STANDING_HMARGIN = 12;
	final int STANDING_VMARGIN = 6;	
	final int STANDING_STAT_MARGIN = 10;	
	
	final int PLAYER_SELECT_ROWS = 6;
	final int PLAYER_SELECT_MAX_NAME_LENGTH = 15;
	final int PLAYER_SELECT_UPPER_TEXT_MARGIN = 78;
	final int PLAYER_SELECT_UPPER_IMAGE_MARGIN = 10;
	final int PLAYER_SELECT_HORIZ_MARGIN = 20;
	
	final int MATCH_VMARGIN = 6;
	final int MATCH_HMARGIN = 12;
	final int MATCH_FIGHTER_WIDTH = 50;
	final int MATCH_FIGHTER_HEIGHT = 50;

	final int STAT_IMAGE_VMARGIN = 2;
	final int STAT_IMAGE_HMARGIN = 2;
	final int STAT_IMAGE_WIDTH = 30;
	final int STAT_IMAGE_HEIGHT = 30;
	
	final int FONT_NUMBER = 6;

	String mode;
	boolean invalidNameEntered = false;
	boolean infoUpdated = true;
	// used to scroll
	int standingsOffset, setOffset;
	int sidebarWidth;
	// settings for different tournament types
	int playersPerMatch, matchesPerSet, fightersPerPlayer;
	// determining the point values for placement
	int[] pointsPerRank;
	int pointsKO, pointsFall, pointsSD;
	// global variables to hold player select screen values
	String selectName;
	int selectedMatch, selectedFighter;
	// interface colors
	int playerNameColor;
	int[] backColor, foreColor, backTextColor, foreTextColor;
	int standingsOdd, standingsOddText, standingsEven, standingsEvenText;
	int summaryColor, completeColor, progressColor, errorColor;
	// interface fonts
	PFont pSelectFont;
	PFont standingsNameFont, standingsStatFont, standingsRankFont, standingsScoreFont;
	PFont matchNameFont, matchStatFont;
	PFont topStatFont;
	// control who is allowed to fight
	ArrayList<String> availFighters;
	HashMap<String,PImage> fighterImg;
	ArrayList<String> selectedFighters;
	// keep track of matchups
	LinkedList<Player> players;
	ArrayList<Match> matches;
	ArrayList<MatchSet> sets;
	StatButton[][][] statButtons;

	public void setup(){
	  frameRate(60);
	  // three different tones of red, blue, yellow, green, orange, light blue, purple, and black ( the colors of each player in smash )
	  backColor=new int[]{ color(102,0,0), color(0,0,102), 
	                    color(102,102,0), color(0,102,0),
	                    color(102,51,0), color(0,102,102),
	                    color(102,0,102), color(0)};
	  foreColor=new int[]{ color(204,0,0), color(0,0,204), 
	                    color(204,204,0), color(0,204,0),
	                    color(204,102,0), color(0,204,204),
	                    color(204,0,204), color(255)};
	  backTextColor = Arrays.copyOf(foreColor, foreColor.length);
	  foreTextColor = Arrays.copyOf(backColor, backColor.length);
	  // the colour for player name text
	  playerNameColor = color(0,54,0);
	  // the colours for the scoreboard
	  standingsOdd = color(232);
	  standingsEven = color(0,63,72);
	  standingsOddText = color(10);
	  standingsEvenText = color(0,242,248);
	  // the colours for the match summary
	  summaryColor = color(40);
	  errorColor = color(54,0,0);
	  completeColor = color(0,54,0);
	  progressColor = color(54,54,0);
	  // the various fonts to be used
	  pSelectFont = makeFont(40,72);
	  standingsNameFont = makeFont(40,30);
	  standingsStatFont = makeFont(40,20);
	  standingsScoreFont = makeFont(40,18);
	  standingsRankFont = makeFont(40,16);
	  matchNameFont = makeFont(40,24);
	  matchStatFont = makeFont(40,18);
	  topStatFont = makeFont(40,14);
	  // initializing arrays to hold all the info
	  String str;
	  availFighters = new ArrayList<String>();
	  fighterImg = new HashMap<String,PImage>();
	  // create an instance of the simple file manager
	  new FileMan(this,true,"Images" + File.separator);
	  players = new LinkedList<Player>();
	  matches = new ArrayList<Match>();
	  sets = new ArrayList<MatchSet>();
	  MatchSet.setMatchList( matches );
	  setMode("Player");
	  resetMatch();
	  resetPlayerSelect();
	  // select the font
	  XML xml = loadXML("smash social roster.xml");
	  if( xml != null ){
	    XML[] fighters = xml.getChildren("fighter");
	    for( int i = 0 ; i < fighters.length ; i++ ){
	      str = fighters[i].getContent();
	      loadFighter(str);
	    }
	  } else {
	    String[] temp = {"Mario","Luigi","Peach", "Bowser","Yoshi","Rosalina",
	         "Bowser Jr","Wario","Game and Watch","DK","Diddy Kong",
	        "Link","Zelda","Sheik","Ganondorf","Toon Link","Samus",
	        "Zero Suit Samus","Pit","Palutena","Marth","Ike",
	        "Robin","Kirby","King Dedede","Meta Knight",
	        "Little Mac","Fox","Falco","Pikachu","Charizard",
	        "Lucario","Jigglypuff","Greninja","Duck Hunt","ROB",
	        "Ness","Captain Falcon","Villager","Olimar",
	        "Wii Fit Trainer","Dr Mario","Dark Pit","Lucina",
	        "Shulk","Pacman","Megaman","Sonic","Mewtwo","Ryu","Roy","Lucas","Random"};
	    for( int i = 0 ; i < temp.length ; i++ ){
	      loadFighter( temp[i] );
	    }
	  }
	  setPlayersPerMatch( 4 );
	  setMatchesPerSet( 4 );
	  fightersPerPlayer = 3;
	  pointsPerRank = new int[]{0,6,4,2,1};
	  pointsKO = 1;
	  pointsFall = -1;
	  pointsSD = -2;
	  setPointSystem( pointsKO, pointsFall, pointsSD, pointsPerRank );
	  //debug( 10, 5 );
	  generateStatButtons();
	  setMode("Stats");
	  //println(PLAYER_WIDTH+(playersPerMatch*MATCH_PLAYER_WIDTH)+(playersPerMatch*matchesPerSet*(2*SIDEBAR_PADDING+SIDEBAR_WIDTH_PER_MATCH))+", "+matchesPerSet*MATCH_HEIGHT+ADD_PLAYER_HEIGHT);
	  //size(PLAYER_WIDTH+(playersPerMatch*MATCH_PLAYER_WIDTH)+(playersPerMatch*matchesPerSet*(2*SIDEBAR_PADDING+SIDEBAR_WIDTH_PER_MATCH)),matchesPerSet*MATCH_HEIGHT+ADD_PLAYER_HEIGHT);
	  //println(width+","+height);
	  //println(fighterImg.size());
	}

	public void debug( int p, int g){
	  for( int i = 0 ; i < p ; i++ ){
		players.add(new Player("Alex","Yoshi"));
		players.add(new Player("Thomas","King Dedede"));
		players.add(new Player("Pidgey","Zero Suit Samus"));
		players.add(new Player("Jacob","Link"));
	  }
	  for( int i = 0 ; i < g ; i++ ){
		generateMatchSet();
	  }
	}
	
	public void findFont(String s){
		for( String t : PFont.list() ){
			if( t.equals(s) ){
				println( s + " exists" );
				return;
			}
		}
		println( s + " does not exist" );
	}
	
	public void loadFighter( String str ){
	  PImage t =  FileMan.fighter(str);
	  if( t != null ){
	    availFighters.add(str);
	    fighterImg.put(str, t);
	  }
	}

	public PFont makeFont(int fontNum, int fontSize){
	  String[] f = PFont.list();
	  return ( fontNum >= 0 && fontNum < f.length ) ? createFont(PFont.list()[fontNum],fontSize) : createFont("Comic Sans MS", 20);
	}
	
	public void setPlayersPerMatch( int n ){
	  playersPerMatch = n;
	  sidebarWidth = playersPerMatch * matchesPerSet * SIDEBAR_BOX_WIDTH;
	  //resize(PLAYER_WIDTH+(playersPerMatch*MATCH_PLAYER_WIDTH)+sidebarWidth,matchesPerSet*MATCH_HEIGHT+ADD_PLAYER_HEIGHT);
	}

	public void setMatchesPerSet( int n ){
	  matchesPerSet = n;
	  sidebarWidth = playersPerMatch * matchesPerSet * SIDEBAR_BOX_WIDTH;
	  //resize(PLAYER_WIDTH+(playersPerMatch*MATCH_PLAYER_WIDTH)+sidebarWidth,matchesPerSet*MATCH_HEIGHT+ADD_PLAYER_HEIGHT);
	}
	
	public void setMode( String mode ){
	  this.mode = mode;
      if( "Player".equals( mode ) ){
	    resetPlayerSelect();
	  }
	}

	public void resetPlayerSelect(){
	  selectName = "";
	  selectedFighter = -1;
	  invalidNameEntered = false;
	}
	
	public void resetMatch(){
	  selectedMatch = -1;
	}

	public void addPlayer(){
	  if( selectName.length() <= PLAYER_SELECT_MAX_NAME_LENGTH && !"".equals(selectName) && selectedFighter >= 0 && selectedFighter < availFighters.size() ){
		players.add(new Player(selectName,availFighters.get(selectedFighter)));
		scrollPlayerStandings(-PLAYER_HEIGHT);
	    resetPlayerSelect();
	  } else {
		  invalidNameEntered = true;
	  }
	}

	public void setPointSystem(int pointsKO, int pointsFall, int pointsSD, int[] pointsPerRank){
	  for( int i = 0 ; i < pointsPerRank.length ; i++ ){
		  RankSystem.setRanking(i,pointsPerRank[i]);
	  }
	  RankSystem.setKOPoints(pointsKO);
	  RankSystem.setFallPoints(pointsFall);
	  RankSystem.setSDPoints(pointsSD);
	}
	
	public void selectPlayer( int mx, int my ){
	  if( overCenter(mx,my) ){
		int x = (int)map( mx, standingsPosition(), sidebarPosition(), 0, 9);
		int y = (int)map( my, 0, playerSelectHeight(), 0, PLAYER_SELECT_ROWS );
		int n = y * playersPerRow() + x;
		if( n < availFighters.size() ){
		  selectedFighter = n;
		} else {
		  selectedFighter = -1;
		}
	  }
	}
	
	public void selectMatchSet( int mx, int my ){
	  if( overMatchUps(mx,my) ){
	    int n = (int)(( my - setOffset)/(float)SIDEBAR_BOX_WIDTH);
	    if( n >= 0 && n < sets.size() ){
      	  selectedMatch = n;
	    }
	  }
	}
	
	public void updateMatch( int mx, int my ){
	  if( selectedMatch >= 0 && selectedMatch < sets.size() ){
		/**
	    int m = (int)(my/(float)MATCH_HEIGHT);
	    int player = ( mx-standingsPosition() ) /MATCH_PLAYER_WIDTH;
	    MatchSet s = sets.get( selectedMatch );
	    Match match = s.match(m);
	    Iterator<Player> players = match.iterator();
	    Player p = null;
	    if( players.hasNext() ) p = players.next();
	    for( int i = 0 ; i < player ; i++ ){
		  if ( players.hasNext() ) p = players.next();
	    }
	    if( p != null ){
	      match.setRank(p,player+1);
	      match.setKO(p,1);
	      match.setFall(p,1);
	      match.setSD(p,1);
	    }
	    */
		for( int i = 0 ; i < statButtons.length ; i++ ){
			for( int j = 0 ; j < statButtons[i].length ; j++ ){
				for( int k = 0 ; k < statButtons[i][j].length ; k++ ){
					statButtons[i][j][k].click(mx, my);
				}
			}
		}
	  }
	}
	
	public MatchSet newMatchSet(){
		MatchSet s = new MatchSet(playersPerMatch,matchesPerSet);
		sets.add( s );
		return s;
	}
	
	public void scramble( ArrayList<Player> p, int n ){
	  Random gen = new Random();
	  int a, b;
	  Player t1, t2;
	  for( int i = 0 ; i < n ; i++ ){
		a = gen.nextInt(p.size());
		b = gen.nextInt(p.size());
		t1 = p.get(a);
		t2 = p.get(b);
		p.set(a, t2);
		p.set(b, t1);
	  }
	}
	
	public void generateMatchSet(){
	  ArrayList<Player> pq = new ArrayList<Player>();
	  HashMap<Player,Integer> rounds = new HashMap<Player,Integer>();
	  int maxRound = 0, r;
	  // loop through every player in every match in every set of matches 
	  for( Player p : players ){
		r = p.stat().ranks().size();
		rounds.put(p, r);
		// increase how many matches the player has played
		if( r > maxRound ){
		  maxRound = r;
		}
	  }
	  maxRound++;
	  for( Player p : players ){
		  for( int i = 0 ; i < maxRound - rounds.get(p) ; i++ ){
			  pq.add(p);
		  }
	  }
	  scramble( pq, pq.size() );
	  MatchSet t;
	  int countNotAllowed;
	  boolean outOfOptions = false;
	  while(!pq.isEmpty()){
		if(sets.size() > 0){
			t = sets.get( sets.size() - 1 );
		} else {
			t = newMatchSet();
		}
		if( t.full() ){
			t = newMatchSet();
		}
		countNotAllowed = 0;
		while( !t.isAllowed( pq.get(0) ) ){
			pq.add(pq.remove(0) );
			countNotAllowed++;
			if( countNotAllowed > pq.size() ){
				outOfOptions = true;
				break;
			}
		}
		if( outOfOptions ){
			println("Fail");
			break;
		}
	    t.add( pq.remove( 0 ) );
	  }
	}
	
	public void generateStatButtons(){
	  statButtons = new StatButton[matchesPerSet][playersPerMatch][4];
	  PVector rankSize = new PVector(60,40), statSize = new PVector(30,40);
	  PVector pos = new PVector(0,0), init = new PVector(0,0);
	  for( int m = 0 ; m < matchesPerSet ; m++ ){
		for( int p = 0 ; p < playersPerMatch ; p++ ){
		  textFont( matchNameFont );
		  init.set( PLAYER_WIDTH+p*MATCH_PLAYER_WIDTH, m*MATCH_HEIGHT + 3*MATCH_VMARGIN + textAscent() );
		  textFont( matchStatFont );
		  pos.set( init.x + MATCH_PLAYER_WIDTH/2, init.y + textAscent() );
	      statButtons[m][p][0] = new RankButton( pos, PVector.add(pos,rankSize), playersPerMatch );
		  pos.set( init.x + MATCH_HMARGIN, init.y + 2*MATCH_VMARGIN + MATCH_FIGHTER_HEIGHT + textAscent() );
	      statButtons[m][p][1] = new KOButton( pos, PVector.add(pos,statSize) );
	      pos.add( MATCH_PLAYER_WIDTH/3.0f, 0 );
	      statButtons[m][p][2] = new FallButton( pos, PVector.add(pos,statSize) );
	      pos.add( MATCH_PLAYER_WIDTH/3.0f, 0 );
	      statButtons[m][p][3] = new SDButton( pos, PVector.add(pos,statSize) );
		}
	  }
	}
	
	public void calculateStandings(){
	  PriorityQueue<Player> pq = new PriorityQueue<Player>();
	  for( int i = players.size() - 1 ; i >= 0 ; i-- ){
		  pq.add( players.removeFirst() );
	  }
	  while(!pq.isEmpty()){
		  players.add( pq.poll() );
	  }
	}
	
	public void displayPlayer( int i ){
	  if( i >= 0 && i < players.size() ){
	  	// keep track of where in the standings we are looking
	    int y = standingsOffset+i*PLAYER_HEIGHT;
	    boolean type = i%2 == 0;
	    noStroke();
	    fill(type?standingsEven:standingsOdd);
        // draw background
	    rect(0,y,PLAYER_WIDTH,PLAYER_HEIGHT);
	    fill(type?standingsEvenText:standingsOddText);
	    // get the player
	    Player p = players.get(i);
	    textFont(standingsNameFont);
	    float textY = STANDING_VMARGIN + textAscent();
	    text( p.name(), STANDING_HMARGIN, y + textY );
	    // position image on the right for even or the left for odd
	    float imgX = type ? standingsPosition()-STANDING_HMARGIN-STANDING_FIGHTER_WIDTH : STANDING_HMARGIN;
	    // add the player offset, then center the fighter's image vertically underneath the player's name in the remaining space available
//	    float imgY = y + (PLAYER_HEIGHT-STANDING_FIGHTER_HEIGHT+2*STANDING_VMARGIN+textAscent())/2;
	    float imgY = y + 2*STANDING_VMARGIN+textAscent();
	    image( FileMan.fighter( p.fighter() ), imgX, imgY, STANDING_FIGHTER_WIDTH, STANDING_FIGHTER_HEIGHT);
	    textFont(standingsStatFont);
	    // place stats on the left for even or on the right for odd
	    float statX = STANDING_HMARGIN + (type ? 0 : 2*STANDING_HMARGIN+STANDING_FIGHTER_WIDTH);
	    float standingGap = STANDING_STAT_MARGIN + textAscent();
	    text("KO: " + p.stat().kos(), statX, y + textY + standingGap);
	    text("Fall: " + p.stat().falls(), statX, y + textY + 2*standingGap);
	    text("SD: " + p.stat().sds(), statX, y + textY + 3*standingGap);
	    textFont(standingsRankFont);
	    text(p.stat().ranks().toString(), statX, y + textY + 4*standingGap);
	    textFont(standingsScoreFont);
	    text("Pts: " + p.stat().score(), imgX, imgY + STANDING_HMARGIN + STANDING_FIGHTER_HEIGHT + textAscent() );
	  }
	}
	
	public void displayStandings(){
	  noStroke();
	  calculateStandings();
	  // display the standings on the left
	  for( int i = 0 ; i < players.size() ; i++ ){
	    displayPlayer(i);
	  }
	  fill(0,83,0);
	  rect(0,height-ADD_PLAYER_HEIGHT,PLAYER_WIDTH,ADD_PLAYER_HEIGHT);
	}
	
	public void displayPlayerSelect(){
	    int rows = PLAYER_SELECT_ROWS;
	    int cols = playersPerRow();
	    float wid = playerSelectWidth()/cols;
	    float hei = playerSelectHeight()/rows;
	    // display all possible fighters
	    int c = 0;
	    for( int i = 0 ; i < rows ; i++ ){
	      for( int j = 0 ; j < cols ; j++ ){
	        if( c >= fighterImg.size() ){ break; }
	        image( fighterImg.get(availFighters.get(c++)), PLAYER_WIDTH+j*wid, i*hei, wid, hei );
	      }
	    }
	    fill(invalidNameEntered?errorColor:playerNameColor);
	    textFont( pSelectFont );
	    text(selectName, PLAYER_WIDTH+PLAYER_SELECT_HORIZ_MARGIN, hei*rows+PLAYER_SELECT_UPPER_TEXT_MARGIN);
	    if( selectedFighter >= 0 && selectedFighter < availFighters.size() ){
	    	image( fighterImg.get(availFighters.get(selectedFighter)), PLAYER_WIDTH+wid*(cols-1)-PLAYER_SELECT_HORIZ_MARGIN,hei*rows+PLAYER_SELECT_UPPER_IMAGE_MARGIN, wid, hei);
	    }
	}

	public void displayMatch( Match match, int matchNum ){
	  boolean even = matchNum%2 == 0;
	  StatButton temp, t[];
	  int player = 0, c;
      for( Player p : match ){
        fill( even ? foreColor[player] : backColor[player] );
        noStroke();
      	int x = PLAYER_WIDTH+player*MATCH_PLAYER_WIDTH;
      	int y = matchNum*MATCH_HEIGHT;
    	rect(x,y,MATCH_PLAYER_WIDTH,MATCH_HEIGHT);
    	fill( even ? foreTextColor[player] : backTextColor[player]);
    	textFont(matchNameFont);
    	text( p.name(), x + MATCH_HMARGIN, y + MATCH_VMARGIN + textAscent() );
      	float bottomTextY = y + 2*MATCH_VMARGIN + textAscent();
    	image(FileMan.fighter(p.fighter()),x+MATCH_HMARGIN,bottomTextY,MATCH_FIGHTER_WIDTH,MATCH_FIGHTER_HEIGHT);
        textFont( matchStatFont );
        text("Rank:", x + MATCH_PLAYER_WIDTH/2, bottomTextY + textAscent() );
        text(match.rank(p), x + 3*MATCH_PLAYER_WIDTH/4.0f, bottomTextY + 3*textAscent() );
        float belowImgY = bottomTextY + 2*MATCH_VMARGIN + MATCH_FIGHTER_HEIGHT;
        text("KO:", x+ MATCH_HMARGIN, belowImgY + textAscent() );
        text(match.ko(p), x+ MATCH_HMARGIN, belowImgY + 3*textAscent() );
        text("Fall:", x + MATCH_HMARGIN + MATCH_PLAYER_WIDTH/3.0f, belowImgY + textAscent() );
        text(match.fall(p), x+ MATCH_HMARGIN + MATCH_PLAYER_WIDTH/3.0f, belowImgY + 3*textAscent() );
        text("SD:", x + MATCH_HMARGIN + 2*MATCH_PLAYER_WIDTH/3.0f, belowImgY + textAscent() );
        text(match.sd(p), x+ MATCH_HMARGIN + 2*MATCH_PLAYER_WIDTH/3.0f, belowImgY + 3*textAscent() );
        t = statButtons[matchNum][player];
        c = even ? foreTextColor[player] : backTextColor[player];
        stroke( red(c), green(c), blue(c), 120 );
        noFill();
        for( int i = 0 ; i < t.length ; i++ ){
          temp = t[i];
          temp.setStat(match.stat(p));
          rect(temp.ul().x, temp.ul().y, temp.size().x, temp.size().y);
        }
        player++;
	  }
	}
	
    // display the current match being looked at
	public void displayMatchSet(int set){
	  if( set >= 0 && set < sets.size() ){
		int i = 0;
		for( Match m : sets.get(set) ){
	      displayMatch( m, i++ );
		}
	  }
	}
	
	public void displayTop(String type, int n, int w){
	  PriorityQueue<Player> pq = new PriorityQueue<Player>();
	  pq.addAll(players);
	  int i = 0;
	  textFont(topStatFont);
	  float offset = textAscent() + 2*STAT_IMAGE_HMARGIN, x, y;;
	  int column;
	  String stat;
	  boolean even;
	  Player p;
	  int h = (matchesPerSet*MATCH_HEIGHT)/n;
	  noStroke();
	  while( i < n && !pq.isEmpty() ){
		stat = "";
		p = pq.poll();
		switch(type){
		  case "KOs":
			  column = 0;
			  stat += p.stat().kos();
			  break;
		  case "Falls":
			  column = 1;
			  stat += p.stat().falls();
			  break;
		  case "SDs":
			  column = 2;
			  stat += p.stat().sds();
			  break;
		  case "1st":
			  column = 3;
			  stat += p.stat().ranks();
			  break;
		  case "2nd":
			  column = 4;
			  stat += p.stat().ranks();
			  break;
		  case "3rd":
			  column = 5;
			  stat += p.stat().ranks();
			  break;
		  case "4th":
			  column = 6;
			  stat += p.stat().ranks();
			  break;
		  default:
			  return;
		}
		even = (i+column) % 2 == 0; 
		fill(even?standingsEven:standingsOdd);
		x = standingsPosition()+column*w+STAT_IMAGE_VMARGIN;
		y = offset + i++*h;
		rect(x,y,w,h);
		image(FileMan.fighter(p.fighter()),x,y,STAT_IMAGE_WIDTH,STAT_IMAGE_HEIGHT);
		fill(even?standingsEvenText:standingsOddText);
		text(p.name(),x, y + STAT_IMAGE_HEIGHT + 2*STAT_IMAGE_VMARGIN + textAscent() );
		text(stat,x + STAT_IMAGE_WIDTH + 2*STAT_IMAGE_HMARGIN, y+textAscent());
	  }
	}
	
	public void displayStats(){
	  HashMap<Player,Integer> gap = new HashMap<Player,Integer>(), rounds = new HashMap<Player,Integer>(), last = new HashMap<Player,Integer>();
	  // int[][] pairs = new int[players.size()][players.size()];
	  // initialize hash table with 0s for every player
	  for( Player p : players ){
		gap.put(p, 0);
		rounds.put(p, 0);
	  }
	  int k = 0, maxGap = 0, tempGap, maxRematch = 0;
	  // loop through every player in every match in every set of matches 
	  for( MatchSet s : sets ){
		for( Match m : s ){
		  for( Player p : m ){
		    // check how long ago the player's last match was
		    tempGap = last.get(p) == null ? k : k - last.get(p);
		    // compare it against their biggest gap between matches
		    gap.put(p, Math.max(tempGap,gap.get(p)));
		    // compare that with longest gap overall
		    maxGap = (tempGap > maxGap) ? tempGap : maxGap;
		    // indicate that the player has played this set
		    last.put(p, k);
		    /**
		    // go through every matchup and count them
		    for( Player p2 : m ){
			  if( p != p2 ){
			    pairs[players.indexOf(p)][players.indexOf(p2)]++;
				maxRematch = Math.max(maxRematch, pairs[players.indexOf(p)][players.indexOf(p2)]);
			  }
		    }
		    */
		  }
		}
		k++;
	  }
	  println( "Longest Gap: ", maxGap );
	  println( "Most Rematches: ", maxRematch );
	  println( "Rounds Played: ", rounds );
	  println( "Gaps: ", gap );
	  int top = 10, w = (sidebarPosition()-standingsPosition())/7;
	  setPointSystem( 1, 0, 0, new int[]{0,0,0,0,0} );
	  displayTop("KOs",top,w);
	  setPointSystem( 0, 1, 0, new int[]{0,0,0,0,0} );
	  displayTop("Falls",top,w);
	  setPointSystem( 0, 0, 1, new int[]{0,0,0,0,0} );
	  displayTop("SDs",top,w);
	  setPointSystem( 0, 0, 0, new int[]{0,1,0,0,0} );
	  displayTop("1st",top,w);
	  setPointSystem( 0, 0, 0, new int[]{0,0,1,0,0} );
	  displayTop("2nd",top,w);
	  setPointSystem( 0, 0, 0, new int[]{0,0,0,1,0} );
	  displayTop("3rd",top,w);
	  setPointSystem( 0, 0, 0, new int[]{0,0,0,0,1} );
	  displayTop("4th",top,w);
	  setPointSystem( pointsKO, pointsFall, pointsSD, pointsPerRank );

	}
	
	public void displayMatchSummary() {
	  int i = 0, j, k;
	  int o = setOffset;
	  // the x coordinate of where the match summary begins
	  int xoff = PLAYER_WIDTH+playersPerMatch*MATCH_PLAYER_WIDTH;
	  // the size with padding included of each match square
	  int w = SIDEBAR_BOX_WIDTH;
	  // check through every set of matches
	  for( MatchSet s : sets ){
	    int status = s.status();
	    switch( status ){
	      case 1:{
	        fill( errorColor );
	        break;
	      }
	      case 2:{
	        fill( completeColor );
	        break;
	      }
	      case 3:{
	        fill( progressColor );
	        break;
	      }
	      default:{
	        fill( summaryColor );
	        break;
	      }
	    }
	    // set the background colour according to the status of the matches
	    noStroke();
	    rect(xoff,o+i*w,playersPerMatch*matchesPerSet*w,w);
	    // print out all the matchups
	    j = 0;
	    for( Match m : s ){
	      k = 0;
	      int n = m.players().size();
	      for( int p = 0 ; p < n ; p++ ){
	        stroke( foreColor[k] );
	        fill( backColor[k] );
	        rect(xoff+w*(j*playersPerMatch+k)+SIDEBAR_PADDING,o+i*w+SIDEBAR_PADDING,SIDEBAR_WIDTH_PER_MATCH,SIDEBAR_WIDTH_PER_MATCH);
	        k++;
	      }
	      j++;
	    }
	    i++;
	  }
	  // allow addition of more matches
	  fill( 72,0,93 );
	  noStroke();
	  rect( xoff, o+i*w, playersPerMatch*matchesPerSet*w, w);
	}

	public void draw(){
	  if( infoUpdated ){
	    background(summaryColor);
	    displayStandings();
	    if( "Player".equalsIgnoreCase( mode ) && fighterImg.size() > 0){
		  displayPlayerSelect();
	    } else if ( "Match".equalsIgnoreCase( mode ) ){
		  displayMatchSet( selectedMatch );
	    } else if( "Stats".equalsIgnoreCase( mode ) ){
	    	displayStats();
	    }
	    displayMatchSummary();
	    infoUpdated = false;
	  }
	}

	public void scrollMatchSummary( int moveY ){
		// the height of the area being used to display standings
  	    int screenHeight = height - ADD_PLAYER_HEIGHT;
	    // scroll the match summary
	    setOffset += moveY; 
	    // the amount of space taken by the match summary
	    int heightUsed = sidebarWidth * (sets.size()+1);
	    // stop the match summary from running away
	    if( setOffset > 0 || heightUsed < screenHeight ){
	      setOffset = 0;
	    } else if( setOffset < screenHeight - heightUsed ){
	      setOffset = screenHeight - heightUsed;
	    }
	}
	
	public void scrollPlayerStandings( int moveY ){
		// the height of the area being used to display standings
  	    int screenHeight = height - ADD_PLAYER_HEIGHT;
	    // the amount of space taken by the match summary
	    int heightUsed = PLAYER_HEIGHT * players.size();
	    // scroll the standings
	    standingsOffset += moveY;
	    // the amount of space being taken up by the standings
	    // ensure the standings don't fly off screen
	    if( standingsOffset > 0 || heightUsed < screenHeight ){
	      standingsOffset = 0;
	    } else if( standingsOffset < screenHeight - heightUsed ){
	      standingsOffset = screenHeight - heightUsed;
	    }
	}
	
	public void mouseDragged(){
	  //determine how much the mouse moved
	  int moveY = mouseY - pmouseY;
	  // how high the screen is
	  if( mouseX > PLAYER_WIDTH+playersPerMatch*MATCH_PLAYER_WIDTH ){
		  scrollMatchSummary(moveY);
	  }
	  if( mouseX < PLAYER_WIDTH ){
		  scrollPlayerStandings( moveY );
	  }
      infoUpdated = true;
	}	
	
	public int playerSelectWidth(){
	  return sidebarPosition()-standingsPosition();
	}
		
	public int playerSelectHeight(){
	  return matchesPerSet*MATCH_HEIGHT-ADD_PLAYER_HEIGHT;
	}

	public int playersPerRow(){
	  return (int)Math.ceil((double)availFighters.size() / PLAYER_SELECT_ROWS);
	}
	
	public int standingsPosition(){
		return PLAYER_WIDTH;
	}
	
	public int sidebarPosition(){
		return width - sidebarWidth;
	}
	
	public boolean overStandingBar( int mx, int my ){
		return mx < standingsPosition();
	}
	
	public boolean overAddPlayer( int mx, int my ){
		return overStandingBar(mx,my) && mouseY > height - ADD_PLAYER_HEIGHT;
	}
	
	public boolean overStandings( int mx, int my ){
		return overStandingBar(mx,my) && !overAddPlayer(mx,my);
	}
	
	public boolean overSidebar( int mx, int my ){
		return mx > sidebarPosition();
	}

	public boolean overGenMatchUp( int mx, int my ){
		return overSidebar(mx,my) && mouseY + setOffset > sets.size()*SIDEBAR_BOX_WIDTH && mouseY + setOffset < (sets.size()+1)*SIDEBAR_BOX_WIDTH;
	}
	
	public boolean overMatchUps( int mx, int my ){
		return overSidebar(mx,my) && mouseY <= sets.size()*SIDEBAR_BOX_WIDTH;
	}
	
	public boolean overCenter( int mx, int my ){
		return !overStandings(mx,my) && !overSidebar(mx,my);
	}
	
	public void mouseClicked(){
	  if( overGenMatchUp( mouseX, mouseY ) ){
	   	generateMatchSet();
	  } else if( overMatchUps( mouseX, mouseY ) ){
		selectMatchSet( mouseX, mouseY );
	   	setMode( "Match" );
	  } else if( overAddPlayer( mouseX, mouseY) ){
		setMode( "Player" );
	  // the center screen is clicked
	  } else {
	    // choose a player
	    if( "Player".equals( mode ) ){
	      selectPlayer( mouseX, mouseY );
	    }
	    // update match data
	    if( "Match".equals( mode ) ){
	      updateMatch( mouseX, mouseY );
	    }
	  }
	  infoUpdated = true;
	}
	
	public void keyPressed(){
	  if( "Player".equals(mode) ){
		//backspace, remove a character
		if( key == 8 ){
		  selectName = selectName.substring(0,Math.max(0, selectName.length()-1));
		// enter, attempt to add player to the list
		} else if ( key == 10 ){
		  addPlayer();
		// add typed character to the player select name
		} else if( key > 31 && key < 127 ){
		  selectName += key;
	  	}
	  } else if ( "Match".equals(mode) ){
		  if( key == 's' ){
			  setMode("Stats");
		  }
	  }
	  infoUpdated = true;
	}
	
	public static void main(String args[]) {
	    PApplet.main(new String[] { "--present", "SmashApp" });
	}
}
