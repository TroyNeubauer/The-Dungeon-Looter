package loader.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.*;

import com.troy.troyberry.opengl.util.BufferUtils;

public class Vbo {
	
	private Vao parent;
	private final int GLID, type;
	
	public Vbo(int GLID, int type) {
		this.GLID = GLID;
		this.type = type;
		bind();
	}
	
	protected void setParent(Vao parent){
		this.parent = parent;
	}
	
	public Vao getParentVao() {
		return parent;
	}
	
	public void bind(){
		GL15.glBindBuffer(type, GLID);
	}
	
	public void unbind(){
		GL15.glBindBuffer(type, 0);
	}

	public void delete() {
		GL15.glDeleteBuffers(GLID);
	}

	public int getType() {
		return type;
	}
}
