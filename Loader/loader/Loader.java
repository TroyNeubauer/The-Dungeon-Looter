package loader;

import loader.mesh.RawMeshData;
import loader.mesh.Vao;

public class Loader {
		
	public static Vao loadToVAO(RawMeshData data){
		if(data == null) return null;
		Vao vao = Vao.newVao();
		if(data.hasIndices()){
			vao.setVertexCount(data.getIndices().length);
			vao.indiciesBuffer(data.getIndices());
		} else {
			vao.setVertexCount(data.getVertices().length / 3);
		}
		vao.vertexBuffer(0, data.getDimensions(), data.getVertices());
		if(data.hastextureCoords()) vao.vertexBuffer(1, 2, data.getTextureCoords());
		
		if(data.hasNormals()) vao.vertexBuffer(2, 3, data.getNormals());
		if(data.hasTangents()) vao.vertexBuffer(3, 3, data.getTangents());
		
		return vao;
	}
}
