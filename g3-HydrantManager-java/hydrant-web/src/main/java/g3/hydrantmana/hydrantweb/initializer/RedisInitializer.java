package g3.hydrantmana.hydrantweb.initializer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import g3.hydrantmana.common.constants.HydrantStatusConstants;
import g3.hydrantmana.domain.entity.Hydrant;
import g3.hydrantmana.hydrantweb.mapper.HydrantMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 在该类加载进IOC容器后，执行一次init方法
 * 初始化写入所有消防栓经纬度的geohash值到redis中
 */
@Component
public class RedisInitializer {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private HydrantMapper hydrantMapper;

    @PostConstruct
    public void init() {
        redisTemplate.delete("geo:hydrant");

        // 添加所有正常消防栓
        QueryWrapper<Hydrant> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", HydrantStatusConstants.NORMAL);
        List<Hydrant> hydrants = hydrantMapper.selectList(queryWrapper);
        for (Hydrant hydrant : hydrants) {
            redisTemplate.opsForGeo().add(
                    "geo:hydrant",
                    new Point(hydrant.getLongitude().doubleValue(), hydrant.getLatitude().doubleValue()),
                    hydrant.getId()
            );
        }
    }
}
