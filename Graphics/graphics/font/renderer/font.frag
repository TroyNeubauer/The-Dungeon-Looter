#version 330

in vec2 pass_textureCoords;

out vec4 out_colour;

uniform vec3 colour;
uniform sampler2D fontAtlas;

uniform float width;
uniform float edge;

uniform float boarderWidth;
uniform float boarderEdge;

uniform vec2 offset;

uniform vec3 outlineColor;

void main(void){

	float distance = 1.0 - texture(fontAtlas, pass_textureCoords).a;
	float alpha = 1.0 - smoothstep(width, width + edge, distance);
	
	float distance2 = 1.0 - texture(fontAtlas, pass_textureCoords + offset).a;
	float outlineAlpha = 1.0 - smoothstep(boarderWidth, boarderWidth + boarderEdge, distance2);
	
	float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
	vec3 overallColor = mix(outlineColor, colour, alpha / overallAlpha); 

	out_colour = vec4(overallColor, overallAlpha);

}