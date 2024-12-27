#version 410 core

layout (position = 0) vec2 vpos2;

void main(){
    gl_Position = vec3(vpos2,0.0f);
}