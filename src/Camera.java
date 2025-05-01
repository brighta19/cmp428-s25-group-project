
public class Camera
{
	//------------------------------------------------------------------------//
	
	static int x = 0;
	static int y = 0;
	
	//------------------------------------------------------------------------//
	
	public static void setLocation(int x, int y)
	{
		Camera.x = x;
		Camera.y = y;
	}
	
	//------------------------------------------------------------------------//
	
	public static void moveUp(int dy)
	{
		y -= dy;
	}
	
	//------------------------------------------------------------------------//
	
	public static void moveDown(int dy)
	{
		y += dy;
	}
	
	//------------------------------------------------------------------------//
	
	public static void moveLeft(int dx)
	{
		//if the player is not between (lx_limit --- lx_limit + 960) and between ( rx_limit --- rx_limit - 960)
		//essentially do nothing if player is close to the edges
		if((PlatformerGame.player.x > (TileMap.maps[TileMap.current].lx_limit + 960)) && (PlatformerGame.player.x < (TileMap.maps[TileMap.current].rx_limit - 960)))
			x -= dx;
	}
	
	//------------------------------------------------------------------------//
	
	public static void moveRight(int dx)
	{
		//if the player is not between (lx_limit --- lx_limit + 960) and between ( rx_limit --- rx_limit - 960)
		//essentially do nothing if player is close to the edges
		if((PlatformerGame.player.x > (TileMap.maps[TileMap.current].lx_limit + 960)) && (PlatformerGame.player.x < (TileMap.maps[TileMap.current].rx_limit - 960)))
			x += dx;
	}
	
	//------------------------------------------------------------------------//
	
}
