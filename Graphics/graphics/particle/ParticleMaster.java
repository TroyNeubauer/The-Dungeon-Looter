package graphics.particle;

import java.util.*;
import java.util.Map.Entry;

import graphics.particle.system.ParticleSystem;
import loader.asset.Texture;
import thedungeonlooter.camera.ICamera;
import thedungeonlooter.gamestate.WorldState;

public class ParticleMaster {

	private static Map<Texture, List<Particle>> particles = new HashMap<Texture, List<Particle>>();
	private static List<ParticleSystem> systems = new ArrayList<ParticleSystem>();
	private static ParticleRenderer renderer;
	private static ICamera camera;

	public static void init(ICamera camera) {
		renderer = new ParticleRenderer(camera);
		ParticleMaster.camera = camera;
	}

	public static void update() {

		Iterator<Entry<Texture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		while (mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();
			Iterator<Particle> i = list.iterator();
			while (i.hasNext()) {
				Particle p = (Particle) i.next();
				if (!p.update(camera) || (p.diesWhenTouchesGround && p.getPosition().y < WorldState.world.getHeight(p.getPosition().x, p.getPosition().z))) {
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
	
	public static List<Particle> getAllParticles(Texture texture){
		return particles.get(texture);
	}

}
