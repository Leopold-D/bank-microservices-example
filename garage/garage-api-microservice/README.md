# Garage API microservice

## Intro

This service is the exposed API facade for the Garage Project, it takes the clients requests and forwards that to the core, it also takes the core answers and sends it back to the client.

## Facade details

{endPoints=[

{[/api/clients/gate/{pRegistration_id}],methods=[DELETE],produces=[application/json]}, 

{[/api/clients/find/{pRegistration_id}],methods=[GET],produces=[application/json]}, 

{[/api/management/status],methods=[GET],produces=[application/json]}, 

{[/api/admin/build/garage],methods=[POST],consumes=[application/json]}, 

{[/api/admin/build/garage],methods=[DELETE]}, 

{[/api/admin/build/level],methods=[POST],consumes=[application/json]}, 

{[/api/admin/build/level/{level_id}],methods=[PUT],consumes=[application/json]}, 

{[/api/admin/build/level],methods=[DELETE]}, 

{[/api/]}, 

{[/api/clients/gate],methods=[POST],consumes=[application/json],produces=[application/json]}, 

{[/endpoints],methods=[GET]}, 

{[/error],produces=[text/html]}, 

{[/error]}

]}

Full Swagger Documentation can be visible at this URL when deployed : http://localhost:8765/garage/swagger-ui.html#/

## Example calls

Please see SOAP UI test to see example calls

For example, the test "ModifyExtendLevel" gives the following status : 

![ModifyExtendLevel](../tests/ModifyExtendLevel.png)

If we move the check one rank up, it fails expecting no free lot while 1 is available : 

![ModifyExtendLevel2](../tests/ModifyExtendLevel2.png)

If a level is deactivated but cars are still present, their lot will be counted in the total lot number until they exit, then the previously occupied lot will not be counted in the total number of lots anymore.

![DesactivateThenAddLevel](../tests/DesactivateThenAddLevel.png)