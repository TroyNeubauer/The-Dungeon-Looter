#version 140

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;

uniform float contrast;
uniform vec3 add;

void main(void){

	out_Colour = texture(colourTexture, textureCoords);
	out_Colour.rgb = (out_Colour.rgb - 0.5) * (1.0 + contrast) + 0.5;
	out_Colour = vec4(add.x + out_Colour.x, add.y + out_Colour.y, add.z + out_Colour.z, out_Colour.z);
	out_Colour = clamp(out_Colour, 0.0, 1.0);

}