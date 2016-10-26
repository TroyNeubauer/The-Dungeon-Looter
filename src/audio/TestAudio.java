package audio;

import com.troy.troyberry.math.Vector3f;

public class TestAudio {

	public static void main(String[] args) throws Exception {
		AudioMaster.init();
		Vector3f position = new Vector3f(8, 2, 0);
		AudioMaster.setListener(new Vector3f());
		int hit = AudioMaster.loadSound("/sounds/hit.wav");
		Source s = new Source();
		s.setLooping(true);
		s.play(hit);

		while (true) {
			position.x -= 0.03f;
			s.setPosition(position);
			Thread.sleep(10);
			System.out.println(position.x);

		}
		//s.delete();
		//AudioMaster.cleanUp();

	}

}
