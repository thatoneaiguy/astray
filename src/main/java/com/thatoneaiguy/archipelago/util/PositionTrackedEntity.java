package com.thatoneaiguy.archipelago.util;

import com.thatoneaiguy.archipelago.render.systems.TrailPoint;

import java.util.List;

public interface PositionTrackedEntity {
    List<TrailPoint> getPastPositions();
}
