//todo: remove ifs
//if the return value is 0, discard

float rect(vec2 texCoord, vec2 border) {
    float dx = distance(texCoord.x, 0);
    dx = min(dx, distance(texCoord.x, 1));
    float dy = distance(texCoord.y, 1);
    dy = min(dy, distance(texCoord.y, 0));

    return ((dx < border.x || dy < border.y) ? 1: 0);
}

float rect(vec2 texCoord, float border) {
    float d = distance(texCoord.x, 0);
    d = min(d, distance(texCoord.x, 1));
    d = min(d, distance(texCoord.y, 1));
    d = min(d, distance(texCoord.y, 0));

    return ((d < border) ? 1: 0);
}

float roundRect(vec2 texCoords, float border, float filled) {
    vec2 vBorder = vec2(border);
    vec2 texCoord = vec2(min(texCoords.x, 1 - texCoords.x), min(texCoords.y, 1 - texCoords.y));

    if(texCoord.x < border && texCoord.y < border) {
        float d = distance(texCoord, vBorder);
        if(d < border) return 1;
        else return 0;
    }

    if(filled == 0 && texCoord.x > border && texCoord.x < 2 * border && texCoord.y > border && texCoord.y < 2 * border) {
        float d = distance(texCoord, 2 * vBorder);
        if(d < border) return 0;
        else return 1;
    }

    return rect(texCoords, vec2((1-filled) * border + 1 * filled));
}

float circle(vec2 texCoords, float innerRadius, float outerRadius) {
    float distance = distance(texCoords, vec2(0.5));
    return (distance < outerRadius && distance >= innerRadius? 1: 0);
}