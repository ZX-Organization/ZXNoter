#version 410 core

layout (location = 0) in vec2 vpos2;

void main(){
    gl_Position = vec4(vpos2,1.0,1.0);
}
