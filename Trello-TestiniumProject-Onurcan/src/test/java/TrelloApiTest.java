import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.get;

public class TrelloApiTest {

    public static String boardID;
    public static String cartID;
    public static String cart1ID;
    public static String cart2ID;

    @Test
    void CreateBoard(){
        RestAssured.baseURI = "https://api.trello.com";
        Response response = given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name","Onurcan")
                .queryParam("key", "6077e11647451ba118e1bd203aec423b")
                .queryParam("token","afdd5ca732a3577e11d23c73a813a99951b1495abce1daff0b03f9343c498b31")
                .post("/1/boards/");

        System.out.println(response.getBody().prettyPrint());

        boardID = response.getBody().jsonPath().getString("id");

        GetList();
        CreateCart();
        UpdateCart();
        DeleteCart();
        DeleteBoard();
    }

    void GetList() {
        baseURI = "https://api.trello.com";
        Response resp1 = given()
                .log().all()
                .queryParam("key", "6077e11647451ba118e1bd203aec423b")
                .queryParam("token", "afdd5ca732a3577e11d23c73a813a99951b1495abce1daff0b03f9343c498b31")
                .get("/1/boards/" + boardID + "/lists");
        System.out.println("Time taken : " + get().time());
        System.out.println("StatusCode : " + get().getStatusCode());
        System.out.println("ContentType : " + get().getContentType());
        resp1.prettyPrint();
        String as = ((ArrayList) resp1.jsonPath().get("id")).get(0).toString();
        String as2 = ((ArrayList) resp1.jsonPath().get("name")).get(0).toString();
        System.out.println("id : " + as + " name: " + as2);
        cartID = as;
    }

    void CreateCart(){
        for (int x=1;x<3;x++){
            RestAssured.baseURI = "https://api.trello.com";
            Response response = given().log().all()
                    //.header("contentType","aContentType.JSON")
                    .contentType(ContentType.JSON)
                    .queryParam("name","Cart"+x)
                    .queryParam("key", "6077e11647451ba118e1bd203aec423b")
                    .queryParam("token","afdd5ca732a3577e11d23c73a813a99951b1495abce1daff0b03f9343c498b31")
                    .queryParam("idList",cartID)
                    .post("/1/cards");
            System.out.println("status code : "+ get().getStatusCode());
            if (x == 1) {
                cart1ID = response.getBody().jsonPath().getString("id");
            }
            else {
                cart2ID = response.getBody().jsonPath().getString("id");
            }
        }
    }

    void UpdateCart(){
        RestAssured.baseURI = "https://api.trello.com";
        List<String > CartId=new ArrayList<>();
        CartId.add(cart1ID);
        CartId.add(cart2ID);
        Random random =new Random();
        int randomCart=random.nextInt(CartId.size());
        given().log().all()
                //.header("contentType","aContentType.JSON")
                .contentType(ContentType.JSON)
                .pathParam("id",CartId.get(randomCart))
                .queryParam("name","deneme")
                .queryParam("desc","açıklama")
                .queryParam("closed","false")
                .queryParam("idMembers","")
                .queryParam("idAttachmentCover","")
                .queryParam("idList",cartID)
                .queryParam("idLabels","")
                .queryParam("idBoard",boardID)
                .queryParam("pos","16384")
                .queryParam("due","")
                .queryParam("dueComplete","false")
                .queryParam("subscribed","false")
                .queryParam("address","")
                .queryParam("locationName","")
                .queryParam("coordinates","")
                .queryParam("key", "6077e11647451ba118e1bd203aec423b")
                .queryParam("token","afdd5ca732a3577e11d23c73a813a99951b1495abce1daff0b03f9343c498b31")
                .put("/1/cards/{id}");
        System.out.println("status code : "+ get().getStatusCode());
    }


    void DeleteCart(){
        baseURI="https://api.trello.com";
        List<String >CartId=new ArrayList<>();
        CartId.add(cart1ID);
        CartId.add(cart2ID);

        for (int x=0;x<CartId.size();x++){
            given().log().all()
                    .contentType(ContentType.JSON)
                    .pathParams("id",CartId.get(x))
                    .queryParam("key", "6077e11647451ba118e1bd203aec423b")
                    .queryParam("token","afdd5ca732a3577e11d23c73a813a99951b1495abce1daff0b03f9343c498b31")
                    .delete("/1/cards/{id}");
        }

        System.out.println("status code : "+ get().getStatusCode());
    }

    void DeleteBoard(){
        baseURI="https://api.trello.com";

        given().log().all()
                .contentType(ContentType.JSON)
                .pathParams("id",boardID)
                .queryParam("key", "6077e11647451ba118e1bd203aec423b")
                .queryParam("token","afdd5ca732a3577e11d23c73a813a99951b1495abce1daff0b03f9343c498b31")
                .delete("/1/boards/{id}");

        System.out.println("status code : "+ get().getStatusCode());
    }
}
