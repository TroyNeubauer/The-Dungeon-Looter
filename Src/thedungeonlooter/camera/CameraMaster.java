package thedungeonlooter.camera;

import thedungeonlooter.gamestate.WorldState;

public class CameraMaster {
	
	private static ICamera currentCamera;
	private static CameraMode currentMode;
	
	public static ICamera init(CameraMode mode){
		return setMode(mode);
	}
	
	public static ICamera setMode(CameraMode mode){
		switch(mode){
			default:
				currentCamera = new FirstPersonCamera();
				currentMode = CameraMode.FIRST_PERSON;
				break;
			case FIRST_PERSON:
				currentCamera = new FirstPersonCamera();
				currentMode = CameraMode.FIRST_PERSON;
				break;
			case THIRD_PERSON:
				currentCamera = new ThirdPersonCamera();
				currentMode = CameraMode.THIRD_PERSON;
				break;
			
		}
		WorldState.setCamera(currentCamera);
		return currentCamera;
	}

	private CameraMaster() {
	}
	
	public static enum CameraMode {
		FIRST_PERSON(1), THIRD_PERSON(3);
		int person;
		CameraMode(int person){
			this.person = person;
		}
	}

	public static CameraMode getMode() {
		return currentMode;
	}

	public static void nextMode() {
		if(currentMode == CameraMode.FIRST_PERSON){
			setMode(CameraMode.THIRD_PERSON);
		}else{
			setMode(CameraMode.FIRST_PERSON);
		}
		
	}

}
