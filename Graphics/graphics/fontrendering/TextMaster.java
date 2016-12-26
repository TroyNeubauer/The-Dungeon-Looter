package graphics.fontrendering;

import java.util.*;

import org.lwjgl.opengl.GL30;

import graphics.fontcreator.*;
import loader.Loader;
import loader.mesh.CustomMesh;

public class TextMaster {
	
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	
	public static void init(){
		renderer = new FontRenderer();
	}
	
	public static void render(){
		renderer.render(texts);
	}
	
	public static void loadText(GUIText text){
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		text.setMeshInfo(new CustomMesh("Text: ",data.toRawMeshData()));
		List<GUIText> textBatch = texts.get(font);
		if(textBatch == null){
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removeText(GUIText text){
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if(textBatch.isEmpty()){
			texts.remove(texts.get(text.getFont()));
			text.getMesh().delete();
		}
	}
	
	public static void cleanUp(){
		renderer.cleanUp();
	}

}
