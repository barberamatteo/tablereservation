package it.matteobarbera.tablereservation.controller.table;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.matteobarbera.tablereservation.Constants;
import it.matteobarbera.tablereservation.http.response.CommonJSONBodies;
import it.matteobarbera.tablereservation.model.table.SimpleTable;
import it.matteobarbera.tablereservation.model.table.TableCRUDException;
import it.matteobarbera.tablereservation.model.table.TableDefinition;
import it.matteobarbera.tablereservation.service.table.TablesDefinitionService;
import it.matteobarbera.tablereservation.service.table.TablesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

import static it.matteobarbera.tablereservation.http.TableAPIInfo.TABLE_CREATED;
import static it.matteobarbera.tablereservation.http.TablesAPIError.NO_SUCH_CATEGORY_DEFINED;
import static it.matteobarbera.tablereservation.http.TablesAPIError.NUMBER_CONFLICT;
import static it.matteobarbera.tablereservation.logging.TableLog.*;

@RestController
@RequestMapping(Constants.ADMIN_TABLES_CRUD_API_ENDPOINT)
public class AdminTablesCRUDRestApiController {

    private static final Logger log = LoggerFactory.getLogger(AdminTablesCRUDRestApiController.class);
    private final TablesService tablesService;
    private final TablesDefinitionService tablesDefinitionService;

    @Autowired
    public AdminTablesCRUDRestApiController(TablesService tablesService, TablesDefinitionService tablesDefinitionService) {
        this.tablesService = tablesService;
        this.tablesDefinitionService = tablesDefinitionService;
    }

    @Operation(
            summary = "Get all tables",
            description = "Returns all the CustomTable objects registered by the admin"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @CrossOrigin
    @GetMapping("/getall/")
    public Set<SimpleTable> getAllTables() {
        Set<SimpleTable> tables = tablesService.getAllTables();
        if (tables.isEmpty()) {
            log.atWarn().log(NO_TABLES_FOUND);
        } else {
            log.atInfo().log(ALL_TABLES_FOUND, tables.size());
        }
        return tables;
    }

    @Operation(
            summary = "Get a table",
            description = "Returns a CustomTable object inputting the number."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Table <num> does not exist.")
    })
    @GetMapping("/getbynum/{num}")
    public SimpleTable getTableByNum(@PathVariable("num") Integer num) {
        Optional<SimpleTable> tableByNum = tablesService.getTableByNum(num);
        if (tableByNum.isPresent()) {
            log.atInfo().log(TABLE_FOUND, tableByNum.get().getId());
        } else {
            log.atInfo().log(NO_TABLE_FOUND_WITH_NUMBER, num);
            throw new TableCRUDException(
                    HttpStatus.NOT_FOUND,
                    num,
                    TableCRUDException.Cause.TABLE_NOT_FOUND
            );
        }
        return tableByNum.get();
    }

    @Operation(
            summary = "Create a table",
            description = "Creates a table in DB, specifying the category (previously defined via the table definition " +
                    "related endpoints), and a number"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Category <category> does not exist. Create a new table definition first."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Table number %d already exists. Please choose another number."
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Table created with number <number> and category <category>"
            )
    })
    @PostMapping("/create/")
    @CrossOrigin
    public ResponseEntity<?> createTable(
            @RequestParam(name = "category") String category,
            @RequestParam(name = "number") int number
    ){


        Optional<TableDefinition> tableDefinitionOptional =
                tablesDefinitionService.getDefByCategory(category);

        if (tableDefinitionOptional.isEmpty()) {
            log.atError().log(NO_CATEGORY_DEFINED, category);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.BAD_REQUEST.value(),
                                    NO_SUCH_CATEGORY_DEFINED.getMessage(category)
                            )
                    );
        }


        Optional<SimpleTable> tableWithSameNumberOptional = tablesService.getTableByNum(number);
        if (tableWithSameNumberOptional.isPresent()) {
            log.atError().log(TABLE_WITH_SAME_NUMBER, number);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            CommonJSONBodies.fromStatusAndMsg(
                                    HttpStatus.BAD_REQUEST.value(),
                                    NUMBER_CONFLICT.getMessage(number)
                            )
                    );
        }


        tablesService.createTable(category.toLowerCase(), number);
        log.atInfo().log(TABLE_CREATED_WITH_NUMBER_AND_CATEGORY, number, category);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        CommonJSONBodies.fromStatusAndMsg(
                                HttpStatus.OK.value(),
                                TABLE_CREATED.getMessage(number, category)
                        )
                );
    }





}
