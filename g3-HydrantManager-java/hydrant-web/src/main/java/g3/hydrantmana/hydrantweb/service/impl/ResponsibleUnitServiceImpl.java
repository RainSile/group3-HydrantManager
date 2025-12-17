package g3.hydrantmana.hydrantweb.service.impl;

import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.dto.ResponsibleUnitDTO;
import g3.hydrantmana.hydrantweb.mapper.ResponsibleUnitMapper;
import g3.hydrantmana.hydrantweb.service.ResponsibleUnitService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ResponsibleUnitServiceImpl implements ResponsibleUnitService {

    @Resource
    private ResponsibleUnitMapper responsibleUnitMapper;

    @Override
    public PageDTO<ResponsibleUnitDTO> listUnit(ResponsibleUnitDTO dto) {
        return new PageDTO<>();
    }

    @Override
    public void contactUnit(ResponsibleUnitDTO dto) {
    }
}
