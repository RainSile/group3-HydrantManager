package g3.hydrantmana.hydrantweb.service;

import g3.hydrantmana.domain.dto.GeoDTO;
import g3.hydrantmana.domain.query.GeoHydrantQuery;
import g3.hydrantmana.domain.query.GeoMinDistanceQuery;

import java.util.List;

public interface GeoInfoService {

    GeoDTO getGeoMinDistance(GeoMinDistanceQuery query);

    List<GeoDTO> findHydrant(GeoHydrantQuery query);

    void addGeoInfo(GeoDTO geoDTO);

    void removeGeoInfo(String hid);
}
