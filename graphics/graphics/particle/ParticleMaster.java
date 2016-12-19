package graphics.particle;

import java.util.*;
import java.util.Map.Entry;

import com.troy.troyberry.math.Matrix4f;

import asset.ParticleTexture;
import asset.Texture;
import camera.ICamera;

public class ParticleMaster {

	private static Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
	private static List<ParticleSystem> systems = new ArrayList<ParticleSystem>();
	private static ParticleRenderer renderer;
	private static ICamera camera;

	public static void init(ICamera camera) {
		renderer = new ParticleRenderer(camera.getProjectionMatrix());
		ParticleMaster.camera = camera;
	}

	public static void update() {

		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		while (mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();
			Iterator<Particle> i = list.iterator();
			while (i.hasNext()) {
				Particle p = (Particle) i.next();
				if (!p.update(camera)) {
					i.remove();
					if (list.isEmpty()) {
						mapIterator.remove();
					}
				}
			}
			InsertionSort.sortHighToLow(list);
		}
		
		Iterator<ParticleSystem> i = systems.iterator();
		while(i.hasNext()){
			ParticleSystem s = (ParticleSystem)i.next();
			if(s.update()){
				i.remove();
				System.out.println("Done with system!");
			}
		}
	}

	public static void render() {

		renderer.render(camera, particles);
	}

	public static void cleanUp() {
		renderer.cleanUp();
	}
	
	public static void addSystem(ParticleSystem system){
		systems.add(system);
	}

	public static void add(Particle particle) {
		List<Particle> list = particles.get(particle.texture);
		if (list == null) {
			list = new ArrayList<Particle>();
			particles.put(particle.texture, list);
		}
		list.add(particle);
	}

	public static int getParticleCount() {
		int counter = 0;
		for (Texture key : particles.keySet()) {
			counter += particles.get(key).size();
		}

		return counter;
	}

}
