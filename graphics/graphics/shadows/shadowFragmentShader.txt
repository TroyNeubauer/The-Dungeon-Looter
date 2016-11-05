#version 330

out vec4 out_colour;

in vec2 textureCoords;

uniform sampler2D modelTexture;//will use this next week

void main(void){
	float alpha = texture(modelTexture, textureCoords).a;
	if(alpha < 0.5){
		discard;
	}
	
	out_colour = vec4(1.0);
	
}