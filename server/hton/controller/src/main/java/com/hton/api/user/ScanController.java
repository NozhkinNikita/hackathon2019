package com.hton.api.user;

import com.hton.api.CredentialUtils;
import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
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
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(value = WebMvcConfig.USER_SCAN_PATH)
@Slf4j
public class ScanController {

    @Autowired
    private CommonDao<Scan, ScanEntity> scanDao;

    @Autowired
    private CommonDao<Location, LocationEntity> locationDao;

    @Autowired
    private CommonDao<UserLocation, UserLocationEntity> userLocationDao;

    @Autowired
    private CommonDao<User, UserEntity> userDao;

    @Autowired
    private CommonDao<Device, DeviceEntity> deviceDao;

    @Autowired
    private CredentialUtils credentialUtils;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getScanById(@PathVariable("id") String id) {
        String login = credentialUtils.getCredentialLogin();

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
                                    .setSearchField("user.login")
                                    .setSearchCondition(SearchCondition.EQUALS)
                                    .setSearchValue(login)
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
        return new ResponseEntity<>(scanDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity createScan(@RequestBody ScanDto scanDto) {
        String login = credentialUtils.getCredentialLogin();

        ComplexCondition condition = new ComplexCondition.Builder()
                .setOperation(Operation.AND)
                .setConditions(new SimpleCondition.Builder()
                                .setSearchField("location.id")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(scanDto.getLocationId())
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
            log.warn("Пользователь с логином " + login + " пытался создать сканирование в неразрешенной локации с id = " + scanDto.getLocationId());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            UserLocation userLocation = userLocations.get(0);

            if (userLocation.getUser().getEnabled()) {
                Scan scan = new Scan();
                scan.setBegin(LocalDateTime.now());
                scan.setStatus(ScanStatus.DRAFT);
                scan.setUserLocation(userLocation);
                scan.setDevice(scanDto.getDevice());
                scan.setPoints(Collections.emptyList());

                Scan savedScan = scanDao.save(scan);
                ScanDto dto = new ScanDto();
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
    public ResponseEntity<?> test(@RequestBody ScanDto scanDto) {
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
