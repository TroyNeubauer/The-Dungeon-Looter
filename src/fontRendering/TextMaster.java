package fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
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

	public static void clear() {
		texts.clear();
	}

	public static void loadText(GUIText text) {
		FontType font = text.getFont();
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
		if (text == null) return;
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(texts.get(text.getFont()));
		}
	}

	public static void cleanUp() {
		renderer.cleanUp();
	}

}
