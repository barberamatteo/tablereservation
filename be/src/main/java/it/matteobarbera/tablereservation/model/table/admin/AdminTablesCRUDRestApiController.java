package it.matteobarbera.tablereservation.model.table.admin;

import it.matteobarbera.tablereservation.Constants;
import it.matteobarbera.tablereservation.http.response.SuccessAdmin;
import it.matteobarbera.tablereservation.model.table.CustomTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(Constants.ADMIN_TABLES_CRUD_API_ENDPOINT)
public class AdminTablesCRUDRestApiController {

    private final TablesService tablesService;
    private final TablesDefinitionService tablesDefinitionService;

    @Autowired
    public AdminTablesCRUDRestApiController(TablesService tablesService, TablesDefinitionService tablesDefinitionService) {
        this.tablesService = tablesService;
        this.tablesDefinitionService = tablesDefinitionService;
    }

    @GetMapping("/getall/")
    public List<CustomTable> getAllTables() {
        List<CustomTable> allTables = tablesService.getAllTables();
        return allTables;
    }

    @GetMapping("/getbynum/{num}")
    public String getTableByNum(@PathVariable("num") Integer num) {
        Optional<CustomTable> tableByNum = tablesService.getTableByNum(num);
        if (tableByNum.isEmpty()) {
            throw new TableCRUDException(
                    HttpStatus.NOT_FOUND,
                    num,
                    TableCRUDException.Cause.TABLE_NOT_FOUND
            );
        }

        return tableByNum.get().toString();
    }

    @PostMapping("/create/")
    public String createTable(
            @RequestParam(name = "category") String category,
            @RequestParam(name = "number") int number
    ){

        Optional<TableDefinition> tableDefinitionOptional =
                tablesDefinitionService.getDefByCategory(category.toLowerCase());

        if (tableDefinitionOptional.isEmpty()) {
            throw new TableCRUDException(
                    HttpStatus.BAD_REQUEST,
                    category,
                    TableCRUDException.Cause.NO_SUCH_CATEGORY_DEFINED
            );
        }

        Optional<CustomTable> tableWithSameNumberOptional = tablesService.getTableByNum(number);
        if (tableWithSameNumberOptional.isPresent()) {
            throw new TableCRUDException(
                    HttpStatus.PRECONDITION_FAILED,
                    number,
                    TableCRUDException.Cause.NUMBER_CONFLICT
            );
        }


        tablesService.createTable(category.toLowerCase(), number);

        ResponseEntity<String> successResponse = new ResponseEntity<>(
                SuccessAdmin.getMessageForAction("createTable"),
                HttpStatus.OK
        );
        return successResponse.getBody();

    }



}
