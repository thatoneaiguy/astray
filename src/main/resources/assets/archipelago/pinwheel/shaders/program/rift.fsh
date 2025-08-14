#define M_PI 3.1415926535897932384626433832795

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;

uniform vec3 pos;          // sphere center in world space
uniform float radius;      // sphere radius
uniform float timer;       // animation timer

uniform mat4 invProjMat;   // inverse projection matrix
uniform mat4 invViewMat;   // inverse view matrix
uniform vec3 cameraPos;    // camera position in world space
uniform float nearPlane;   // camera near plane
uniform float farPlane;    // camera far plane

in vec2 texCoord;
out vec4 fragColor;

float depthToViewZ(float depthSample) {
    float z = depthSample * 2.0 - 1.0;
    return 2.0 * nearPlane * farPlane / (farPlane + nearPlane - z * (farPlane - nearPlane));
}

vec3 screenToViewPos(vec2 uv, float depth) {
    vec4 clipPos = vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    vec4 viewPos = invProjMat * clipPos;
    viewPos /= viewPos.w;
    return viewPos.xyz;
}

vec3 screenToWorldPos(vec2 uv, float depth) {
    vec3 viewPos = screenToViewPos(uv, depth);
    vec4 worldPos = vec4(cameraPos, 0.0) + invViewMat * vec4(viewPos, 1.0);
    return worldPos.xyz;
}

float blockSnap(vec3 worldPos) {
    vec3 snapped = floor(worldPos) + 0.5;
    vec3 diff = abs(worldPos - snapped);
    float edgeThreshold = 0.1;
    return step(max(diff.x, max(diff.y, diff.z)), edgeThreshold);
}

void main() {
    vec3 sceneColor = texture(DiffuseSampler0, texCoord).rgb;
    float depthSample = texture(DiffuseDepthSampler, texCoord).r;

    if (depthSample == 1.0) {
        fragColor = vec4(sceneColor, 1.0);
        return;
    }

    vec3 worldPos = screenToWorldPos(texCoord, depthSample);
    float dist = distance(worldPos, pos);

    float lineWidth = 0.05;
    float line = step(abs(dist - radius), lineWidth);

    float snapLine = blockSnap(worldPos);

    float finalLine = line * snapLine;

    vec3 finalColor = mix(sceneColor, vec3(1.0), finalLine);

    fragColor = vec4(finalColor, 1.0);
}
