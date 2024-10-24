package soa.lab2.orgmanager.controller;

import com.example.api.OrgmanagerApi;
import com.example.model.OrgmanagerHireIdPost200Response;
import com.example.model.OrgmanagerHireIdPostRequest;
import com.example.model.OrgmanagerMergeId1Id2NewNameNewAddressPost200Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soa.lab2.orgmanager.service.OrgManagerService;

import javax.validation.Valid;

@RestController
@RequestMapping("/orgmanager")
// TODO: создать ControllerAdvice exceptionService
public class OrgManagerController implements OrgmanagerApi {

    private final OrgManagerService orgManagerService;

    public OrgManagerController(OrgManagerService orgManagerService) {
        this.orgManagerService = orgManagerService;
    }

    @Override
    public ResponseEntity<OrgmanagerHireIdPost200Response> orgmanagerHireIdPost(
            @PathVariable("id") Integer id,
            @Valid @RequestBody OrgmanagerHireIdPostRequest orgmanagerHireIdPostRequest) {

        orgManagerService.hireEmployee(id, orgmanagerHireIdPostRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrgmanagerMergeId1Id2NewNameNewAddressPost200Response> orgmanagerMergeId1Id2NewNameNewAddressPost(
            @PathVariable("id1") Integer id1,
            @PathVariable("id2") Integer id2,
            @PathVariable("new-name") String newName,
            @PathVariable("new-address") String newAddress) {

        orgManagerService.mergeOrganizations(id1, id2, newName, newAddress);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
