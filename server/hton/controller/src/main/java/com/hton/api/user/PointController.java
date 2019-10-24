package com.hton.api.user;

import com.hton.api.CredentialUtils;
import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.api.requests.PointCreateRequest;
import com.hton.api.requests.PointUpdateRequest;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.Condition;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.Point;
import com.hton.domain.Router;
import com.hton.domain.RouterData;
import com.hton.domain.Scan;
import com.hton.domain.UserLocation;
import com.hton.entities.PointEntity;
import com.hton.entities.RouterEntity;
import com.hton.entities.ScanEntity;
import com.hton.service.LocationValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
@RequestMapping(value = WebMvcConfig.USER_POINT_PATH)
public class PointController {

    @Autowired
    private CommonDao<Point, PointEntity> pointDao;

    @Autowired
    private CommonDao<Scan, ScanEntity> scanDao;

    @Autowired
    private LocationValidatorService locationValidatorService;

    @Autowired
    private CommonDao<Router, RouterEntity> routerDao;

    @Autowired
    private CredentialUtils credentialUtils;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getPointById(@PathVariable("id") String id) {
        return new ResponseEntity<>(pointDao.getById(id), HttpStatus.OK);
    }

    // TODO dikma когда нам нужно удалять точки?
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> removePointById(@PathVariable("id") String id) {
        pointDao.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getPoints(@RequestParam(required = false) String filter) {
        Condition condition = FilterUtils.parseFilter(filter);
        condition.setMaskFields(Arrays.asList("id", "name", "begin", "end", "isRepeat", "scanId"));
        return new ResponseEntity<>(pointDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createPoint(@RequestBody PointCreateRequest request) {
        Scan scan = scanDao.getById(request.getScanId());

        if (scan.getUserLocation() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            String login = credentialUtils.getCredentialLogin();

            Optional<UserLocation> userLocation = locationValidatorService.validateLocation(login, scan.getUserLocation().getLocation().getId());

            if (userLocation.isPresent()) {
                Point point = new Point();
                point.setName(request.getName());
                point.setIsRepeat(false);
                point.setScanId(request.getScanId());

                Point savedPoint = pointDao.save(point);

                PointCreateRequest result = new PointCreateRequest();
                result.setId(savedPoint.getId());

                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updatePoint(@RequestBody PointUpdateRequest request) {
        Point point = pointDao.getById(request.getId());

        if (point == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Scan scan = scanDao.getById(point.getScanId());

            if (scan.getUserLocation() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                String login = credentialUtils.getCredentialLogin();

                Optional<UserLocation> userLocation = locationValidatorService.validateLocation(login, scan.getUserLocation().getLocation().getId());

                if (userLocation.isPresent()) {
                    Condition condition = new SimpleCondition.Builder()
                            .setSearchField("locationId")
                            .setSearchCondition(SearchCondition.EQUALS)
                            .setSearchValue(scan.getUserLocation().getLocation().getId())
                            .build();

                    List<Router> routers = routerDao.getByCondition(condition);

                    Map<String, String> ourRouters = routers.stream().collect(Collectors.toMap(Router::getBssid, Router::getId));

                    request.getRouterDatas().forEach(p -> {
                        p.setOurRouterId(ourRouters.get(p.getBssid()));
                        p.setPointId(point.getId());
                    });

                    List<String> removeRouterDataIds = point.getRouterDatas().stream().map(RouterData::getId).collect(Collectors.toList());

                    point.setBegin(request.getBegin());
                    point.setEnd(request.getEnd());
                    point.setIsRepeat(!point.getRouterDatas().isEmpty());
                    point.setRouterDatas(request.getRouterDatas());

                    pointDao.update(point, removeRouterDataIds);

                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
        }
    }
}
