# URL Lookup

## Intro

Our website is using speaking URLs to give users and bots a good impression of what 
the page is about, e.g. 
● http://www.choucroute.com/Clothing/Women/ 

● http://www.choucroute.com/Boat­Shoes/ 
 
All these combinations are mapped in a table with the following format: 
 
from  to 
/products  /Fashion/ 
/products?gender=female  /Women/ 
/products?gender=female&tag=1234  /Women/Shoes/ 
/products?tag=5678  /Boat­Shoes 
/products?brand=123  /Adidas/ 
...   ...  
 
You can see it as key value map between combinations of parameters and speaking 
URLs. 
 
Use cases for this mapping table are the lookup of a parameter combination as soon as 
you request a speaking URL and a reverse lookup of the speaking URL, whenever you 
are building a link. 
 
Please implement a scaling and performant solution to lookup 200.000 URLs in both 
ways. The mapping table itself is stored in a database, the lookup should not request 
the database because of performance reasons. 
 
 
Little extra: 
If you have enough time add the following functionality: When you are building a link 
and you can’t find an exact match, use the one that matches best.  
 Example: 
You are building a link for the following parameter combination:  
● /products?gender=female&tag=1234&tag=5678 
 
The mapping table doesn’t contain an exact match for it, so the best match would be: 
● /Women/Shoes/?tag=5678 

## Definition lacks

- No example base provided (200K entries)
- No query params number
- URLs and Query seems inconsistent from a REST PoV

## Implementation

- With no real input file for the DB, loading will be hardcoded : http://localhost:8765/urlmapper/init/

- "products" will be considered as base, any URL with query param will start by "/products?<param>..."

- The mapper is mapping without knowing if the final string returned can be a valid URL, meaning :

*products  => Fashion*
*gender=female  => Women*
*tag=1234  => Shoes*
*tag=5678  => Boat­Shoes*
*brand=123  => Adidas*

With this way of doing, we tacle the upper issue : 

*/products?gender=female&tag=1234&tag=5678*

Gives : 

*/Women/Shoes/Boat­Shoes*

Which seems to be correct knowing the fact that BoatShoes is a sub-category of Shoes.

- To be able to check an URL validity, a graph referencing elements ID (easy & query style) and their parents/children should be created,
once the different String couples mapped to their ID in a list representing the URL (*1/2/6/8/1543213/9* for ie), it's pretty easy to browse the
graph and check if the URL is valid. 

The task being a Mapper, and this URL Validator approach being different, it should only be 
used for hardening site browsing. In a common use case, if the endpoint isn't available, a BAD_REQUEST could be returned directly 
at service level.

## Notice

- Endpoint is unsecured
- Code should be optimized for prod
- This is a PoC !
- Tests are missing

## Video

[URL Mapper Example](http://youtu.be/MZZW0rFk1Rc?hd=1)

## Example calls

http://localhost:8765/urlmapper/endpoint/** - ** Being the task url

http://localhost:8765/urlmapper/init - Loads with 4 values
http://localhost:8765/urlmapper/init200K - Loads with 1 000 005 values
http://localhost:8765/urlmapper/size - Give the dataset size
