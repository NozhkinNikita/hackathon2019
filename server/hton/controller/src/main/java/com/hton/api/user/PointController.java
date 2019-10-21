package com.hton.api.user;

import com.hton.api.CredentialUtils;
import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.api.requests.CreatePointRequest;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.Condition;
import com.hton.domain.Point;
import com.hton.domain.Scan;
import com.hton.domain.UserLocation;
import com.hton.entities.PointEntity;
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
import java.util.Collections;
import java.util.Optional;

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
    private CredentialUtils credentialUtils;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getPointById(@PathVariable("id") String id) {
        return new ResponseEntity<>(pointDao.getById(id), HttpStatus.OK);
    }

    // TODO dikma когд нам нужно удалять точки?
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> removePointById(@PathVariable("id") String id) {
        pointDao.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getPoints(@RequestParam(required = false) String filter) {
        Condition condition = FilterUtils.parseFilter(filter);
        condition.setMaskFields(Arrays.asList("id", "name", "begin", "end", "isRepeat"));
        return new ResponseEntity<>(pointDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createPoint(@RequestBody CreatePointRequest request) {
        Scan scan = scanDao.getById(request.getScanId());

        if (scan.getUserLocation() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            String login = credentialUtils.getCredentialLogin();

            Optional<UserLocation> result = locationValidatorService.validateLocation(login, scan.getUserLocation().getLocation().getId());

            if (result.isPresent()) {

                Point point = new Point();
                point.setBegin(request.getBegin());
                point.setEnd(request.getEnd());
                point.setName(request.getName());
                point.setIsRepeat(false);
                // TODO dikma не сохраняется идентификатор сканирования
                point.setScanId(request.getScanId());
                // TODO dikma временно
                point.setRouterDates(Collections.emptyList());

                pointDao.save(point);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updatePoint(@RequestBody Point point) {
        // TODO dikma будет изменена сигнатура
        pointDao.update(point);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
