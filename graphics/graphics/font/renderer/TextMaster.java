package graphics.font.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graphics.font.loader.FontType;
import graphics.font.loader.GUIText;
import graphics.font.loader.TextMeshData;
import loader.Loader;

public class TextMaster {

	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;

	public static void init() {
		renderer = new FontRenderer();
	}

	public static void render() {
		renderer.render(texts);
	}

	public static void render(GUIText textToRender) {
		Map<FontType, List<GUIText>> text = new HashMap<FontType, List<GUIText>>();
		if (text == null)
			return;
		FontType font = textToRender.getFont();
		if (font == null)
			return;
		TextMeshData data = font.loadText(textToRender);
		int vao = Loader.getLoader().loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		textToRender.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = new ArrayList<GUIText>();
		text.put(font, textBatch);
		renderer.render(text);
	}

	public static void loadText(GUIText text) {
		if (text == null)
			return;
		FontType font = text.getFont();
		if (font == null)
			return;
		TextMeshData data = font.loadText(text);
		int vao = Loader.getLoader().loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		} else {
			if (textBatch.contains(text)) {
				return;
			}
		}
		textBatch.add(text);
	}

	public static void removeText(GUIText text) {
		if (text == null)
			return;
		List<GUIText> textBatch = texts.get(text.getFont());
		if (textBatch == null)
			return;
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(texts.get(text.getFont()));
		}
	}

	public static void cleanUp() {
		renderer.cleanUp();
	}

	public static void clear() {
		texts.clear();

	}

}
