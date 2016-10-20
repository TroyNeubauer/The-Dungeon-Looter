#version 140

in vec3 textureCoords;
out vec4 out_Color;

uniform samplerCube night;
uniform float blendFactor;

void main(void){
	vec4 sykColor = vec4(0.464, 0.6601, 0.9921, 1.0);
	vec4 texture2 = texture(night, textureCoords);
		
	
    out_Color = mix(sykColor, texture2, blendFactor);
}