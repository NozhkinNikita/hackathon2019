package com.hton.api.user;

import com.hton.api.CredentialUtils;
import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.api.requests.CreateScanRequest;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.*;
import com.hton.domain.*;
import com.hton.entities.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping(value = WebMvcConfig.USER_SCAN_PATH)
@Slf4j
public class ScanController {

    @Autowired
    private CommonDao<Scan, ScanEntity> scanDao;

    @Autowired
    private CommonDao<UserLocation, UserLocationEntity> userLocationDao;

    @Autowired
    private CommonDao<Device, DeviceEntity> deviceDao;

    @Autowired
    private CredentialUtils credentialUtils;

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

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> removeScanById(@PathVariable("id") String id) {
        scanDao.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getScans(@RequestParam(required = false) String filter) {
        User user = credentialUtils.getUserInfo();
        Condition condition = FilterUtils.getFilterWithLogin(filter, "userLocation.userId", user.getId());
        condition.setMaskFields(Arrays.asList("id", "begin", "end", "status"));
        return new ResponseEntity<>(scanDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity createScan(@RequestBody CreateScanRequest createScanRequest) {
        String login = credentialUtils.getCredentialLogin();

        ComplexCondition condition = new ComplexCondition.Builder()
                .setOperation(Operation.AND)
                .setConditions(new SimpleCondition.Builder()
                                .setSearchField("location.id")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(createScanRequest.getLocationId())
                                .build(),
                        new SimpleCondition.Builder()
                                .setSearchField("user.login")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(login)
                                .build()
                )
                .build();

        List<UserLocation> userLocations = userLocationDao.getByCondition(condition);

        if (userLocations.isEmpty()) {
            log.warn("Пользователь с логином " + login + " пытался создать сканирование в неразрешенной локации с id = " + createScanRequest.getLocationId());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            UserLocation userLocation = userLocations.get(0);

            if (userLocation.getUser().getEnabled()) {
                Scan scan = new Scan();
                scan.setBegin(LocalDateTime.now());
                scan.setStatus(ScanStatus.DRAFT);
                scan.setUserLocation(userLocation);
                scan.setDevice(createScanRequest.getDevice());
                scan.setPoints(Collections.emptyList());

                Scan savedScan = scanDao.save(scan);
                CreateScanRequest dto = new CreateScanRequest();
                dto.setBegin(savedScan.getBegin());
                dto.setId(savedScan.getId());

                return new ResponseEntity<>(dto, HttpStatus.CREATED);
            } else {
                log.warn("Пользователь с логином " + login + " заблокирован");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> test(@RequestBody CreateScanRequest createScanRequest) {
        Device device = new Device();
        device.setModel("model");
        device.setMac("qweqw");
        device.setIpV4("3847327");
        deviceDao.save(device);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PutMapping(value = "/")
//    public ResponseEntity<?> updateScan(@RequestBody Scan scan) {
//        scanDao.update(scan);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
