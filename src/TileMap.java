import java.awt.*;
import java.io.*;

public class TileMap extends RoomBase
{

	//tracks which map player is in and the map
	static int current = 0;
	static int numMaps = 0;
	static TileMap[] maps = new TileMap[1];
	
	static Player player = new Player(50, 50);
	
	//for periods and characters you seen in the file
	String[] map;
	
	Image[]  tile;
	String[] tile_name;
	
	
	Image  background;
	int    repeatBackground; 
	String background_name;
	
	Rect   bounds[];

	int scale;
	
	//------------------------------------------------------------------------//
	
	public TileMap(String fileName,int scale)
	{
		super(pressing);
		this.scale = scale;
		loadMap(fileName);
		loadAssets();
		maps[numMaps] = this;
		numMaps++;
	}
	
	@Override
	public void inGameLoop() {

	}
	
	//------------------------------------------------------------------------//
   // Load TileMap data from a text file                                     //
	//------------------------------------------------------------------------//
	
	public void loadMap(String fileName)
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
		   
		   // Load Tile Fileames
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
   // Draw the TileMap                                                       //
	//------------------------------------------------------------------------//
		
	public void draw(Graphics pen)
	{
		for( int i = 0; i < repeatBackground; i++) {
			pen.drawImage(background,  (i * 1920) +  (- Camera.x / 5) ,  - Camera.y / 5, 1920, 1080, null);
		}
		
		for(int row = 0; row < maps[current].map.length; row++)
		{	
			for(int col = 0; col < map[row].length(); col++)
			{
				char c = map[row].charAt(col);				
				
				if(c != '.')
				{	
					//to check if the bounds are correct
					for(Rect rect: bounds) { rect.draw(pen); }
					
					pen.drawImage(maps[current].tile[c - 'A'], scale*col - Camera.x, scale*row - Camera.y, scale, scale, null);
				}
			}
		}
		
	}
	
	//------------------------------------------------------------------------//
   // Convenience method for loading images                                  //
	//------------------------------------------------------------------------//
	
	public Image getImage(String filename)
	{
		return Toolkit.getDefaultToolkit().getImage(filename);
	}
	
	public Rect[] getBounds() {
		return maps[current].bounds;
	}

}