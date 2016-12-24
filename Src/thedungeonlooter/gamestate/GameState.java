package thedungeonlooter.gamestate;

public interface GameState {
	
	public void  render();
	
	public void update(int updateCount);
	
	public void onStart();
	
	public void onEnd();
	
	public void cleanUp();

}
