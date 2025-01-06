package it.matteobarbera.tablereservation.model.table.admin;

import it.matteobarbera.tablereservation.Constants;
import it.matteobarbera.tablereservation.http.response.SuccessAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(Constants.ADMIN_TABLESDEFINITION_CRUD_API_ENDPOINT)
public class AdminTablesDefinitionCRUDRestApiController {

    private final TablesDefinitionService tablesDefinitionService;

    @Autowired
    public AdminTablesDefinitionCRUDRestApiController(TablesDefinitionService tablesDefinitionService) {
        this.tablesDefinitionService = tablesDefinitionService;
    }


    @PostMapping("/define/")
    public String defineNewCategory(
            @RequestParam("category") String category,
            @RequestParam("standaloneCapacity") Integer standaloneCapacity
    ){
        Optional<TableDefinition> existingCategory = tablesDefinitionService.getDefByCategory(category.toLowerCase());

        if (existingCategory.isPresent()) {
            throw new TableDefinitionCRUDException(
                    HttpStatus.PRECONDITION_FAILED,
                    category,
                    TableDefinitionCRUDException.Cause.EXISTING_CATEGORY_NAME
            );
        }

        tablesDefinitionService.createNewDef(category.toLowerCase(), standaloneCapacity);
        ResponseEntity<String> successResponse = new ResponseEntity<>(
                SuccessAdmin.getMessageForAction("createDefinitionTable"),
                HttpStatus.OK
        );
        return successResponse.getBody();
    }
}
