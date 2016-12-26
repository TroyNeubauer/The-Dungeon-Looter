package loader.mesh;

import com.troy.troyberry.util.MyFile;

import loader.mesh.*;
import loader.request.ResourceRequest;

public class OBJFileMesh extends Mesh implements ResourceRequest {
	
	private static final String RES_LOCATION = "/objects/";
	
	private MyFile path;
	private boolean normalMapped;

	public OBJFileMesh(MyFile path, boolean normalMapped) {
		super("Mesh: " + path.getName());
		this.path = path;
		this.normalMapped = normalMapped;
	}
	
	public OBJFileMesh(String path, boolean normalMapped) {
		this(new MyFile(RES_LOCATION + path + ".obj"), normalMapped);
	}

	@Override
	public void doResourceRequest() {
		if(normalMapped){
			data = NormalMappedObjLoader.loadOBJ(path);
		} else {
			data = OBJFileLoader.loadOBJ(path);
		}
	}

}
