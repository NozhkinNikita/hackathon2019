package com.hton.api.user;

import com.hton.api.CredentialUtils;
import com.hton.api.FilterUtils;
import com.hton.api.WebMvcConfig;
import com.hton.dao.CommonDao;
import com.hton.dao.filters.ComplexCondition;
import com.hton.dao.filters.Condition;
import com.hton.dao.filters.Operation;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.Scan;
import com.hton.entities.Role;
import com.hton.entities.ScanEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = WebMvcConfig.USER_SCAN_PATH)
public class ScanController {

    @Autowired
    private CommonDao<Scan, ScanEntity> scanDao;

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
        String login = credentialUtils.getCredentialLogin();
        Condition condition = FilterUtils.getFilterWithLogin(filter, login);
        return new ResponseEntity<>(scanDao.getByCondition(condition), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> createScan(@RequestBody Scan scan) {
        scanDao.save(scan);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> updateScan(@RequestBody Scan scan) {
        scanDao.update(scan);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
