package app;

import static spark.Spark.*;
import service.CelularService;


public class Aplicacao {
	
	private static CelularService celularService = new CelularService();

    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        post("/celular/insert", (request, response) -> celularService.insert(request, response));

        get("/celular/:id", (request, response) -> celularService.get(request, response));
        
        get("/celular/list/:orderby", (request, response) -> celularService.getAll(request, response));

        get("/celular/update/:id", (request, response) -> celularService.getToUpdate(request, response));
        
        post("/celular/update/:id", (request, response) -> celularService.update(request, response));
           
        get("/celular/delete/:id", (request, response) -> celularService.delete(request, response));

             
    }
}