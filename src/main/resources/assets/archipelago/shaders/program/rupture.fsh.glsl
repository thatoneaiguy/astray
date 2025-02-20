#version 150

uniform sampler2D DiffuseSampler;
uniform vec4 TargetColor;
uniform vec4 NewColor;
uniform float Tolerance;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 col = texture(DiffuseSampler, texCoord);

    float dist = distance(col.rgb, TargetColor.rgb);

    if (dist < Tolerance) {
        fragColor = vec4(NewColor.rgb, col.a);
    } else {
        fragColor = col;
    }
}
