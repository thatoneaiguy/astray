#version 330 core

in vec2 vUv; // UV coordinates
out vec4 fragColor; // Output color

uniform sampler2D uTexture; // The texture of the block
uniform vec4 uColor; // Color of the glass

void main() {
    // Sample the texture color
    vec4 texColor = texture(uTexture, vUv);

    // Check the alpha value of the texture
    // If the alpha value is less than a threshold, discard the fragment
    if (texColor.a < 0.1) {
        discard; // Do not render blocks behind this glass
    }

    discard;

    // Set the output color, multiplying by the glass color
    fragColor = texColor * uColor;
}