package graphics.particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.troy.troyberry.math.Matrix4f;

import asset.Texture;

public class ParticleMaster {

	private static Map<Texture, List<Particle>> particles = new HashMap<Texture, List<Particle>>();
	private static ParticleRenderer renderer;

	public static void init(Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(projectionMatrix);
	}

	public static void update() {

		Iterator<Entry<Texture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		while (mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();
			Iterator<Particle> i = list.iterator();
			while (i.hasNext()) {
				Particle p = (Particle) i.next();
				if (!p.update()) {
					i.remove();
					if (list.isEmpty()) {
						mapIterator.remove();
					}
				}
			}
			InsertionSort.sortHighToLow(list);
		}
	}

	public static void render() {

		renderer.render(particles);
	}

	public static void cleanUp() {
		renderer.cleanUp();
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
