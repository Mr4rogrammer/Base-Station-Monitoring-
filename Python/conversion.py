from math import pi, cos, sin, sqrt, atan2, radians
def offset_coords_metres(lat, lng, north_offset, east_offset):
    # Earth's radius, sphere
    R = 6378137

    # Coordinate offsets in radians
    d_lat = north_offset / R
    d_lng = east_offset / (R * cos(pi * lat / 180))

    # Offset position, decimal degrees
    lat_o = lat + (d_lat * 180) / pi
    lng_o = lng + (d_lng * 180) / pi

    return [lat_o, lng_o]
