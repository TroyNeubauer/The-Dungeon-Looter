#version 330

in vec2 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform vec2 translation;
uniform float xSpread;

void main(void){

	gl_Position = vec4(position.x + translation.x * 2.0 * xSpread, position.y + translation.y * -2.0, 0.0, 1.0);
	pass_textureCoords = textureCoords;

}