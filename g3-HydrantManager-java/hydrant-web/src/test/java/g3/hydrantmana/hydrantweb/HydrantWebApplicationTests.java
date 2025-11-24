package g3.hydrantmana.hydrantweb;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.*;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;

import java.awt.*;
import java.util.List;

@Slf4j
@SpringBootTest
class HydrantWebApplicationTests {

    @Resource
    RedisTemplate<String,String> redisTemplate;

    @Test
    void testGeoAdd() {
        double longtitude = -122.376662;
        double latitude = -22.523791;
        long rows = redisTemplate.opsForGeo()
                .add("geo:hydrant",new Point(longtitude,latitude),"1992205916723748833");
        log.info("添加情况：{}",rows);
    }

    @Test
    void testGeoGet(){
        List<Point> position = redisTemplate.opsForGeo().position("geo:hydrant", "1757481893114638336");
        log.info("位置信息：{}",position);
    }

    @Test
    void testGeoRemove(){
        Long rows = redisTemplate.opsForGeo().remove("geo:hydrant", "1992205916723748833");
        log.info("删除情况：{}",rows);
    }

    @Test
    void testDistance(){
        // 默认为米，使用Metrics限定单位
        Distance distance = redisTemplate.opsForGeo()
                .distance("geo:hydrant", "1757481893114638336", "1992205916723748833", Metrics.KILOMETERS);
        log.info("距离：{}",distance);
    }

    @Test
    // 根据给定经纬度搜索指定半径的ZSet成员
    void testRadius(){
        double longtitude = -122.376662;
        double latitude = -23.023791;
        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius(
                "geo:hydrant",
                new Circle(new Point(longtitude, latitude), new Distance(100, Metrics.KILOMETERS)),
                // 指定返回额外携带参数，还可以进行排序，通过这个方法可以实现查询最近消防栓
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                        .includeDistance()     // 返回距离
                        .includeCoordinates()  // 返回经纬度
                        .sortAscending()       // 按距离排序
        );
        // GeoResults: [averageDistance: 0.0, results: GeoResult [content: RedisGeoCommands.GeoLocation(name=1992205916723748833, point=null), distance: 0.0, ]]
        log.info("查询结果集：{}",results);
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results){
            // [content: RedisGeoCommands.GeoLocation(name=1992205916723748833, point=null), distance: 0.0, ]
            log.info("遍历该结果集：{}",result);
            // 1992205916723748833
            log.info("范围内的消防栓ID：{}",result.getContent().getName());
        }
    }
}
