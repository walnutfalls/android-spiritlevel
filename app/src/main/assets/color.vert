uniform mat4 u_ModelViewProjection;

attribute vec4 a_Position;
attribute vec4 a_Color;

varying vec4 v_Color;

void main()
{
	v_color = a_Color;
	gl_Position = u_ModelViewProjection * a_Position;
}