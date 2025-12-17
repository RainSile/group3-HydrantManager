package g3.hydrantmana.hydrantweb.service;

import g3.hydrantmana.domain.dto.GeoDTO;
import g3.hydrantmana.domain.dto.MaintenanceDTO;
import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.dto.ResponsibleUnitDTO;
import g3.hydrantmana.domain.query.MaintenanceQuery;

public interface ResponsibleUnitService {

    PageDTO<ResponsibleUnitDTO> listUnit(ResponsibleUnitDTO dto);

    void contactUnit(ResponsibleUnitDTO dto);
}
