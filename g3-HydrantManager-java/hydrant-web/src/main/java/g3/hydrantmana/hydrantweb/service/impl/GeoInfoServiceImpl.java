package g3.hydrantmana.hydrantweb.service.impl;

import g3.hydrantmana.common.exceptions.HydrantOnMarsException;
import g3.hydrantmana.common.exceptions.OperationFailedException;
import g3.hydrantmana.domain.dto.GeoDTO;
import g3.hydrantmana.domain.query.GeoHydrantQuery;
import g3.hydrantmana.domain.query.GeoMinDistanceQuery;
import g3.hydrantmana.hydrantweb.service.GeoInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class GeoInfoServiceImpl implements GeoInfoService {

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public GeoDTO getGeoMinDistance(GeoMinDistanceQuery query) {
        double longitude = query.getLongitude().doubleValue();
        double latitude = query.getLatitude().doubleValue();
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius(
                "geo:hydrant",
                new Circle(new Point(longitude, latitude), new Distance(1000, Metrics.KILOMETERS)),
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                        .includeDistance()     // 返回距离
                        .includeCoordinates()  // 返回经纬度
                        .sortAscending()       // 按距离排序
                        .limit(1)   // 只要最近的
        );
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = results.getContent();
        if (content.isEmpty()) {
            throw new HydrantOnMarsException("附近 1000 千米内没有找到消防栓,无法定位最近的消防栓位置");
        }
        GeoResult<RedisGeoCommands.GeoLocation<String>> loc = content.get(0);
        GeoDTO geoDTO = new GeoDTO();
        geoDTO.setHid(loc.getContent().getName());
        geoDTO.setLongitude(BigDecimal.valueOf(loc.getContent().getPoint().getX()));
        geoDTO.setLatitude(BigDecimal.valueOf(loc.getContent().getPoint().getY()));
        geoDTO.setDistance(loc.getDistance().getValue());
        return geoDTO;
    }

    @Override
    public List<GeoDTO> findHydrant(GeoHydrantQuery query) {
        double longitude = query.getLongitude().doubleValue();
        double latitude = query.getLatitude().doubleValue();
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius(
                "geo:hydrant",
                new Circle(new Point(longitude, latitude), new Distance(query.getDistance(), Metrics.KILOMETERS)),
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                        .includeDistance()     // 返回距离
                        .includeCoordinates()  // 返回经纬度
                        .sortAscending()       // 按距离排序
        );
        List<GeoDTO> dtoList = new ArrayList<>();
        results.getContent().forEach(geoResult -> {
            GeoDTO geoDTO = new GeoDTO();
            geoDTO.setHid(geoResult.getContent().getName());
            geoDTO.setLongitude(BigDecimal.valueOf(geoResult.getContent().getPoint().getX()));
            geoDTO.setLatitude(BigDecimal.valueOf(geoResult.getContent().getPoint().getY()));
            geoDTO.setDistance(geoResult.getDistance().getValue());
            dtoList.add(geoDTO);
        });
        if (dtoList.isEmpty()) {
            throw new HydrantOnMarsException("没有找到任何消防栓");
        }
        return dtoList;
    }

    @Override
    public void addGeoInfo(GeoDTO geoDTO) {
        Long valBack = redisTemplate.opsForGeo().add(
                "geo:hydrant",
                new Point(geoDTO.getLongitude().doubleValue(), geoDTO.getLatitude().doubleValue()),
                geoDTO.getHid()
        );
        log.warn("添加键值返回值: {}",valBack);
    }

    @Override
    public void removeGeoInfo(String hid) {
        Long valBack = redisTemplate.opsForGeo().remove("geo:hydrant", hid);
        log.warn("删除键值返回值: {}",valBack);
    }
}
