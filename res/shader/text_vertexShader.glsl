#version 430

//layout(location = 0) uniform mat4 trans;

layout(location = 0) in vec4 cPosition;
layout(location = 1) in vec2 cTexCoord;
layout(location = 2) in vec4 posOffset;
layout(location = 3) in vec4 spriteSheetBounds;
layout(location = 4) in vec3 inColor;
layout(location = 5) in float charIndex;
layout(location = 6) in float wobbleStrength;

layout(location = 100) uniform sampler2D atlas;
layout(location = 200) uniform float translationX;
layout(location = 201) uniform float translationY;
layout(location = 202) uniform float textBoxX;
layout(location = 203) uniform float textBoxY;
layout(location = 204) uniform float textBoxWidth;
layout(location = 205) uniform float textBoxHeight;
layout(location = 206) uniform float windowWidth;
layout(location = 207) uniform float windowHeight;
layout(location = 208) uniform float ticks;
layout(location = 209) uniform float maxChars;
layout(location = 210) uniform float writerProgess;

out vec4 characterPosition;
out vec4 textBoxSize;
out vec3 color;
out vec2 fragTexCoord;
out float charID;

//import util_pixelation

vec2 random2( vec2 p ) {
    return fract(sin(vec2(dot(p,vec2(127.1,311.7)),dot(p,vec2(269.5,183.3))))*43758.5453);
}

vec2 atlasUv(vec2 uv, sampler2D spriteSheet, vec4 bounds, float flipX, float flipY) {
    vec2 atlasSize = textureSize(spriteSheet, 0);
    vec2 range = bounds.zw;
    vec2 texPos = (vec2(flipX * (1 - uv.x) + (1 - flipX) * (uv.x), flipY * (1 - uv.y) + (1 - flipY) * (uv.y)) * range + bounds.xy);
    texPos = vec2(texPos.x / atlasSize.x, texPos.y / atlasSize.y);
    return texPos;
}

void main()
{
    vec2 shakey = random2((spriteSheetBounds.xy + vec2(1 + charIndex)) * ticks) * 0.002;
    shakey = vec2(shakey.x, shakey.y * 1.4) * 0;

    float wobble = sin((ticks + charIndex * 2) * 0.1f) * wobbleStrength;

    vec2 offset = vec2(posOffset.x / windowWidth, posOffset.y / windowHeight);
    mat4 transformation = mat4(
        posOffset.z / windowWidth, 0, 0, 0,
        0, posOffset.w / windowHeight, 0, 0,
        0, 0, 1, 0,
        offset.x + translationX, offset.y + translationY, 0, 1
    );

    vec2 textBoxStart = vec2(((translationX + 1) / 2) * windowWidth, ((translationY + 1) / 2) * windowHeight);  //middle of first letter

    characterPosition = vec4(textBoxStart + posOffset.xy, posOffset.zw);
    textBoxSize = vec4(textBoxX - textBoxWidth / 2, textBoxY - textBoxHeight / 2, textBoxWidth * 2, textBoxHeight * 2);
    gl_Position = transformation * cPosition + vec4(0 + shakey.x, 0 + wobble + shakey.y, 0, 0);

    fragTexCoord = atlasUv(cTexCoord, atlas, spriteSheetBounds, 0, 1);

    charID = charIndex;
    color = inColor;
}