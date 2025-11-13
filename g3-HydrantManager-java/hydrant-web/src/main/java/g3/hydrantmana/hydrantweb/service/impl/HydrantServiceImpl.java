package g3.hydrantmana.hydrantweb.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import g3.hydrantmana.domain.dto.HydrantDTO;
import g3.hydrantmana.domain.dto.PageDTO;
import g3.hydrantmana.domain.entity.Hydrant;
import g3.hydrantmana.domain.query.HydrantQuery;
import g3.hydrantmana.hydrantweb.mapper.HydrantMapper;
import g3.hydrantmana.hydrantweb.service.HydrantService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HydrantServiceImpl implements HydrantService {
    @Resource
    HydrantMapper hydrantMapper;

    /**
     * 查询消防栓(条件+分页)
     * @param query
     * @return
     */
    @Override
    public PageDTO<HydrantDTO> listHydrant(HydrantQuery query) {
        Page<HydrantDTO> page = new Page<>(query.getPageIndex(), query.getPageSize());
        hydrantMapper.selectHydrant(page, query);
        return PageDTO.of(page);
    }

    /**
     * 添加消防栓
     * @param hydrantDTO
     */
    @Override
    public void addHydrant(HydrantDTO hydrantDTO) {
        Hydrant hydrant = new Hydrant();
        BeanUtils.copyProperties(hydrantDTO,hydrant);
        hydrantMapper.insert(hydrant);
        return;
    }

    /**
     * 移除消防栓
     * @param id
     */
    @Override
    public void removeHydrant(String id) {
        int affectedRows = hydrantMapper.deleteById(id);
        if (affectedRows == 0){
            try {
                throw new Exception("数据不存在");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 更新消防栓
     * @param hydrantDTO
     */
    @Override
    public void changeHydrant(HydrantDTO hydrantDTO) {
        int affectedRows = hydrantMapper.updateHydrant(hydrantDTO);
        if (affectedRows == 0){
            try{
                throw new Exception("没有受影响行数");
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
