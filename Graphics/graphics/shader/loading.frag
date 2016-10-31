#version 140

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;

uniform float contrast;

void main(void){

	out_Colour = texture(colourTexture, textureCoords);

}