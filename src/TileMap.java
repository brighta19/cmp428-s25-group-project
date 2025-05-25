import java.awt.*;
import java.io.*;

public class TileMap extends RoomBase
{
	//tracks which map player is in and the map
	static int current = 0;
	static int numMaps = 0;
	static TileMap[] maps = new TileMap[2];
	
	//for periods and characters you seen in the file
	String[] map;
	
	Image[]  tile;
	String[] tile_name;
	
	
	Image  background;
	int    repeatBackground; 
	String background_name;
	
	Rect   bounds[];

	int scale;
	
	//the maps left and right x's where the camera cant go any further
	int lx_limit;
	int rx_limit;
	
	
		
	//------------------------------------------------------------------------//
	
	public TileMap(String filename,int scale)
	{
		super(pressing);
		this.scale = scale;
		loadMaps(filename);
		loadAssets();
		maps[numMaps] = this;
		numMaps++;
	}
	
	@Override
	public void inGameLoop() {}
	
	//------------------------------------------------------------------------//
   // Load TileMap data from a text file                                     //
	//------------------------------------------------------------------------//
	
	public void loadMaps(String fileName)
	{
		
		File file = new File(fileName);
	
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(file));
			   
		   // Load Map Codes
		   int n = Integer.parseInt(input.readLine());  
		   
		   map = new String[n];
		   
		   for(int row = 0; row < n; row++)
		   {
			   map[row] = input.readLine();
		   }
		   
		   // Load Tile Filenames
		   n = Integer.parseInt(input.readLine());
		   
		   tile_name = new String[n];
		   
		   for(int i = 0; i < n; i++)
		   {
			   tile_name[i] = input.readLine();
		   }
		   
		   // how many times the background needs to be repeated
		   repeatBackground = Integer.parseInt(input.readLine());
		   
		   // Load Background Filename
		   background_name = input.readLine();	
		   
		   //loads the number of bounds in the room
		   bounds = new Rect[Integer.parseInt(input.readLine())];
		   int x;
		   int y;
		   int w;
		   int h;
		   
		   //loads x, y, w, h in that order and adds it to the array 
		   for(int i = 0; i < bounds.length; i++) {
			   x = Integer.parseInt(input.readLine());
			   y = Integer.parseInt(input.readLine());
			   w = Integer.parseInt(input.readLine());
			   h = Integer.parseInt(input.readLine());
			  
			   bounds[i] = new Rect( scale  * x, scale * y, scale * w, scale * h);
			   
			   //to skip the gap, makes the text file more readable
			   input.readLine();
		   }
		   
		   
		   //the points in which player and camera can't go further
		   lx_limit = (Integer.parseInt(input.readLine())) * scale;
		   rx_limit = (Integer.parseInt(input.readLine())) * scale;
		   
	
		   input.close();
		}
		catch(IOException x) {};	
	}
	
	//------------------------------------------------------------------------//
   // Load Images for Tiles and Background as indicated TileMap data files   // 
	//------------------------------------------------------------------------//
	
	public void loadAssets()
	{		
	   tile = new Image[tile_name.length];

	   for(int i = 0; i < tile.length; i++)
		{
			tile[i] = getImage(tile_name[i]);
		}
		background = getImage(background_name);		
	}
	
		//------------------------------------------------------------------------//
	   // Convenience method for loading images                                  //
		//------------------------------------------------------------------------//
		
	public Image getImage(String filename) {
		return Toolkit.getDefaultToolkit().getImage(filename);
	}

	
	public Rect[] getBounds() {
		return bounds;
	}
		
	public void changeMap(int room) {
		if( room >= 0 && room < maps.length ) 
		{
			System.out.println("room changed");
			current = room;
		}
	}

	//checks if the player is near the edge of the map to do a transition
	//if so transitions automatically
	/*public void checkIfNearEdge(Player player) {
		 if( player.x < (maps[current].getLeftLimit() - 10) && (current != 0)) {
//	        	changeCurrent(current - 1);
	        	player.x = maps[current].getRightLimit() - 20;
	        	Camera.x = maps[current].getRightLimit() - 1920;
	     }
		 if( player.x > (maps[current].getRightLimit() - 10) && (current != 1)) {
//	        	changeCurrent(current + 1);
	        	player.x = maps[current].getLeftLimit();
	        	Camera.x = maps[current].getLeftLimit();
		 }
	}*/

	public void checkIfNearEdge(Player player) {
		if (player.x < (maps[current].getLeftLimit() - 10) && current > 0) {
			current--;
			player.x = maps[current].getRightLimit() - 20;
			player.old_x = maps[current].getRightLimit() - 20;
			Camera.x = maps[current].getRightLimit() - 1920;

			((PlatformerGame) GameBase.instance).level = current + 1;
		}

		if (player.x > (maps[current].getRightLimit() - 10) && current < maps.length - 1) {
			current++;
			player.x = maps[current].getLeftLimit();
			player.old_x = maps[current].getLeftLimit();
			Camera.x = maps[current].getLeftLimit();

			PlatformerGame game = (PlatformerGame) GameBase.instance;
			game.level = current + 1;
			game.respawnEnemies();
		}
	}


	public int getLeftLimit() { return lx_limit; }
	public int getRightLimit() { return rx_limit; }

	//------------------------------------------------------------------------//
   // Draw the TileMap                                                       //
	//------------------------------------------------------------------------//
		
	public void draw(Graphics pen)
	{
		for( int i = 0; i < repeatBackground; i++) {
			pen.drawImage(background,  (i * 1920) +  (- Camera.x / 5) ,  - Camera.y / 5, 1920, 1080, null);
		}
		
		for(int row = 0; row < map.length; row++)
		{	
			for(int col = 0; col < map[row].length(); col++)
			{
				char c = map[row].charAt(col);				
				
				if(c != '.')
				{	
					//to check if the bounds are correct
					for(Rect rect: bounds) { rect.draw(pen); }
					
					pen.drawImage(tile[c - 'A'], scale*col - Camera.x, scale*row - Camera.y, scale, scale, null);
				}
			}
		}
		
	}

}
