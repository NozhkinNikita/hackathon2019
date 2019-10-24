package com.hton.api.user;

import com.hton.api.CredentialUtils;
import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.api.requests.ScanCreateRequest;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.ComplexCondition;
import com.hton.dao.filters.Condition;
import com.hton.dao.filters.Operation;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.Scan;
import com.hton.domain.User;
import com.hton.domain.UserLocation;
import com.hton.entities.Role;
import com.hton.entities.ScanEntity;
import com.hton.entities.ScanStatus;
import com.hton.service.LocationValidatorService;
import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@CrossOrigin
@Controller
@RequestMapping(value = WebMvcConfig.USER_SCAN_PATH)
@Slf4j
public class ScanController {

    @Autowired
    private CommonDao<Scan, ScanEntity> scanDao;

    @Autowired
    private CredentialUtils credentialUtils;

    @Autowired
    private LocationValidatorService locationValidatorService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getScanById(@PathVariable("id") String id) {
        User user = credentialUtils.getUserInfo();

        if (credentialUtils.getCredentialRoles().contains(Role.NETWORK_ADMIN)) {
            return new ResponseEntity<>(scanDao.getById(id), HttpStatus.OK);
        } else {
            ComplexCondition condition = new ComplexCondition.Builder()
                    .setOperation(Operation.AND)
                    .setConditions(new SimpleCondition.Builder()
                                    .setSearchField("id")
                                    .setSearchCondition(SearchCondition.EQUALS)
                                    .setSearchValue(id)
                                    .build(),
                            new SimpleCondition.Builder()
                                    .setSearchField("userLocation.userId")
                                    .setSearchCondition(SearchCondition.EQUALS)
                                    .setSearchValue(user.getId())
                                    .build()
                    )
                    .build();

            return new ResponseEntity<>(
                    scanDao.getByCondition(condition).stream().findFirst().orElse(null), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getScans(@RequestParam(required = false) String filter) {
        User user = credentialUtils.getUserInfo();
        Condition condition = FilterUtils.getFilterWithLogin(filter, "userLocation.userId", user.getId());
        condition.setMaskFields(Arrays.asList("id", "begin", "end", "status", "device", "userLocation.location"));
        return new ResponseEntity<>(scanDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity createScan(@RequestBody ScanCreateRequest request) {
        String login = credentialUtils.getCredentialLogin();
        Optional<UserLocation> userLocation = locationValidatorService.validateLocation(login, request.getLocationId());

        if (userLocation.isPresent()) {
            Scan scan = new Scan();
            scan.setBegin(LocalDateTime.now());
            scan.setStatus(ScanStatus.DRAFT);
            scan.setUserLocation(userLocation.get());
            scan.setDevice(request.getDevice());
            scan.setPoints(Collections.emptyList());

            Scan savedScan = scanDao.save(scan);
            ScanCreateRequest result = new ScanCreateRequest();
            result.setBegin(savedScan.getBegin());
            result.setId(savedScan.getId());

            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> finishScan(@PathVariable("id") String id) {
        Scan scan = scanDao.getById(id);

        if (scan.getUserLocation() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            String login = credentialUtils.getCredentialLogin();
            Optional<UserLocation> result = locationValidatorService.validateLocation(login, scan.getUserLocation().getLocation().getId());

            if (result.isPresent()) {
                scan.setStatus(ScanStatus.FINISHED);
                scanDao.update(scan);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> removeScanById(@PathVariable("id") String id) {
        Scan scan = scanDao.getById(id);

        if (scan.getUserLocation() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            String login = credentialUtils.getCredentialLogin();
            Optional<UserLocation> result = locationValidatorService.validateLocation(login, scan.getUserLocation().getLocation().getId());

            if (result.isPresent()) {
                scanDao.remove(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }
}
