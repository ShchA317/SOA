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
    @PostMapping("/merge/{id1}/{id2}/{new-name}/{new-address}")
    public ResponseEntity<OrgmanagerMergeId1Id2NewNameNewAddressPost200Response> orgmanagerMergeId1Id2NewNameNewAddressPost(
            @PathVariable("id1") Integer id1,
            @PathVariable("id2") Integer id2,
            @PathVariable("new-name") String newName,
            @PathVariable("new-address") String newAddress) {

        int id = orgManagerService.mergeOrganizations(id1, id2, newName, newAddress);
        OrgmanagerMergeId1Id2NewNameNewAddressPost200Response response =
                new OrgmanagerMergeId1Id2NewNameNewAddressPost200Response()
                        .newOrgId(id)
                        .message("Поздравляем, вы создали корпорацию тысячелетия!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
