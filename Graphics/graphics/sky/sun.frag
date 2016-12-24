#version 140

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D sunTexture;

void main(void){

	out_Color = texture(sunTexture, textureCoords) * 1.5;

}