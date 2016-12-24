package thedungeonlooter.frustumculling;

import com.troy.troyberry.math.Vector3f;

import thedungeonlooter.camera.ICamera;

public class FrustumCuller {
	private static final int NUM_PLANES = 5;

	private Frustum frustum;
	private Plane[] planes = new Plane[NUM_PLANES];

	public FrustumCuller(ICamera camera) {
		this.frustum = new Frustum(camera);
		initPlanes();
		constructPlanes();
	}

	public void update(ICamera camera) {
		frustum.update(camera);
		constructPlanes();
	}

	public boolean isInFrustum(Vector3f center, float radius) {
		if(radius < 0.0f)throw new IllegalArgumentException("Radius cannot be null!");
		for (Plane plane : planes) {
			if (plane.getSignedDistance(center) < -radius) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isInFrustum(Vector3f mins, Vector3f maxs) {
		for (int i = 0; i < NUM_PLANES; i++) {
			Plane plane = planes[i];
			float pX = plane.getNormal().x > 0 ? maxs.x : mins.x;
			float pY = plane.getNormal().y > 0 ? maxs.y : mins.y;
			float pZ = plane.getNormal().z > 0 ? maxs.z : mins.z;
			float pointDistance = plane.getNormal().x * pX + plane.getNormal().y * pY + plane.getNormal().z * pZ;
			if(pointDistance < -plane.getConstant()){
				return false;
			}
		}
		return true;
	}

	private void initPlanes() {
		for (int i = 0; i < planes.length; i++) {
			planes[i] = new Plane();
		}
	}

	private void constructPlanes() {
		planes[0].setPlane(frustum.getVertex(0), frustum.getVertex(4), frustum.getVertex(5));
		planes[1].setPlane(frustum.getVertex(0), frustum.getVertex(2), frustum.getVertex(6));
		planes[2].setPlane(frustum.getVertex(1), frustum.getVertex(3), frustum.getVertex(2));
		planes[3].setPlane(frustum.getVertex(7), frustum.getVertex(6), frustum.getVertex(2));
		planes[4].setPlane(frustum.getVertex(1), frustum.getVertex(5), frustum.getVertex(7));
	}
}
