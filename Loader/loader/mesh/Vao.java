package loader.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.*;

import com.troy.troyberry.opengl.util.BufferUtils;

public class Vao {
	
	private final int GLID;
	private int vertexCount;
	private final List<Vbo> buffers;
	private boolean bound = false;

	public Vao(int GLID, int vertexCount) {
		this.GLID = GLID;
		this.vertexCount = vertexCount;
		this.buffers = new ArrayList<Vbo>();
		bind();
	}
	
	public void addBuffer(Vbo buffer){
		buffer.setParent(this);
		buffers.add(buffer);
	}
	
	public void bind() {
		if(!bound){
			GL30.glBindVertexArray(GLID);
			bound = true;
		}
	}
	
	
	public void unbind() {
		if(bound){
			GL30.glBindVertexArray(0);
			bound = false;
		}
	}
	
	public void delete(){
		unbind();
		for(Vbo vbo: buffers){
			vbo.delete();
		}
		GL30.glDeleteVertexArrays(GLID);
	}
	
	public int getGLID() {
		return GLID;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	/**
	 * Creates a new VBO with the desired data and adds it to the parent VAO
	 * @param attributeNumber 
	 * 		-The index that this VBO will be stored into the VAO
	 * @param coordinateSize 
	 * 		-The size of the coordinates of this data (x, y, z, w) = 4, (x, y, z) = 3, (x, y) = 2 etc.
	 * @param data
	 * 		-The data that will be stored in this VBO
	 * @param bufferType
	 * 		The draw type that Open GL will use to optimize this buffer one of <br><table><tr><td>{@link GL15#GL_STREAM_DRAW STREAM_DRAW}</td><td>{@link GL15#GL_STREAM_READ STREAM_READ}</td><td>{@link GL15#GL_STREAM_COPY STREAM_COPY}</td><td>{@link GL15#GL_STATIC_DRAW STATIC_DRAW}</td><td>{@link GL15#GL_STATIC_READ STATIC_READ}</td><td>{@link GL15#GL_STATIC_COPY STATIC_COPY}</td><td>{@link GL15#GL_DYNAMIC_DRAW DYNAMIC_DRAW}</td></tr><tr><td>{@link GL15#GL_DYNAMIC_READ DYNAMIC_READ}</td><td>{@link GL15#GL_DYNAMIC_COPY DYNAMIC_COPY}</td></tr></table>
	 * @return
	 * 		the new VBO
	 */
	public Vbo vertexBuffer(int attributeNumber, int coordinateSize, float[] data, int bufferType) {
		this.bind();
		Vbo vbo = new Vbo(GL15.GL_ARRAY_BUFFER, GL15.glGenBuffers());
		this.addBuffer(vbo);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data);
		GL15.glBufferData(vbo.getType(), buffer, bufferType);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		vbo.unbind();
		return vbo;
	}
	
	public Vbo vertexBuffer(int attributeNumber, int coordinateSize, float[] data){
		return vertexBuffer(attributeNumber, coordinateSize, data, GL15.GL_STATIC_DRAW);
	}
	
	public Vbo indiciesBuffer(int[] indices, int bufferType) {
		this.bind();
		Vbo vbo = new Vbo(GL15.GL_ELEMENT_ARRAY_BUFFER, GL15.glGenBuffers());
		this.addBuffer(vbo);
		IntBuffer buffer = BufferUtils.createIntBuffer(indices);
		GL15.glBufferData(vbo.getType(), buffer, bufferType);
		vbo.unbind();
		return vbo;
	}
	
	public Vbo indiciesBuffer(int[] indices){
		return indiciesBuffer(indices, GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Returns weather or not this vao is the one currently bound to Open GL
	 */
	public boolean isBound() {
		return bound;
	}
	
	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}
	
	/**
	 * Allocates a new VAO with the no vertex count
	 * WARNING: the vertex count must be set later
	 * @return The new Vao
	 */
	public static Vao newVao(){
		return new Vao(GL30.glGenVertexArrays(), -1);
	}
	
	/**
	 * Allocates a new VAO with the desired vertexCount
	 * @return The new Vao
	 */
	public static Vao newVao(int vertexCount){
		return new Vao(GL30.glGenVertexArrays(), vertexCount);
	}
	
	

}
