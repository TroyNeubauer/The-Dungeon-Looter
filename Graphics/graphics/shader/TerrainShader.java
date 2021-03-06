package graphics.shader;

import java.util.List;

import com.troy.troyberry.math.*;

import thedungeonlooter.camera.ICamera;
import thedungeonlooter.entity.light.Light;

public class TerrainShader extends ShaderProgram {

	private static final int MAX_LIGHTS = 4;
	private static final String VERTEX_FILE = "/graphics/shader/terrain.vert";
	private static final String FRAGMENT_FILE = "/graphics/shader/terrain.frag";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColour[];
	private int location_shineDamper;
	private int location_attenuation[];
	private int location_reflectivity;
	private int location_skyColour;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	private int location_plane;
	private int location_toShadowMapSpace;
	private int location_shadowMap;
	private int location_shadowDistance;
	private int location_pcfCount;
	private int location_mapSize;
	private int location_density;
	private int location_gradient;
	private int location_shadowsEnabled;
	private int location_skyBlend;

	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		location_plane = super.getUniformLocation("plane");
		location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
		location_shadowMap = super.getUniformLocation("shadowMap");
		location_shadowDistance = super.getUniformLocation("shadowDistance");
		location_pcfCount = super.getUniformLocation("pcfCount");
		location_mapSize = super.getUniformLocation("mapSize");
		location_density = super.getUniformLocation("density");
		location_gradient = super.getUniformLocation("gradient");
		location_shadowsEnabled = super.getUniformLocation("shadowsEnabled");

		location_skyBlend = super.getUniformLocation("skyBlend");

		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColour = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}

	public void connectTextureUnits() {
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
		super.loadInt(location_shadowMap, 5);
	}

	public void loadFogValues(float density, float gradient) {
		super.loadFloat(location_density, density);
		super.loadFloat(location_gradient, gradient);
	}

	public void loadShadowMapSize(int size) {
		super.loadInt(location_mapSize, size);
	}

	public void loadShadowDistance(float f) {
		super.loadFloat(location_shadowDistance, f);
	}

	public void loadPCFCount(int count) {
		super.loadInt(location_pcfCount, count);
	}

	public void loadToShadowSpace(Matrix4f matrix) {
		super.loadMatrix(location_toShadowMapSpace, matrix);
	}

	public void loadSkyBlendFactor(float blend) {
		super.loadFloat(location_skyBlend, blend);
	}

	public void loadClipPlane(Vector4f clipPlane) {
		super.loadVector(location_plane, clipPlane);
	}

	public void loadSkyColor(float r, float g, float b) {
		super.loadVector(location_skyColour, new Vector3f(r, g, b));
	}

	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColour[i], lights.get(i).getColor());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
				super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}

	public void loadViewMatrix(ICamera camera) {
		super.loadMatrix(location_viewMatrix, camera.getViewMatrix());
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}

	public void enableShadows(boolean enabled) {
		super.loadBoolean(location_shadowsEnabled, enabled);
	}

}
