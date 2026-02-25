package gift.cucumber.steps;

import gift.cucumber.ScenarioContext;
import gift.model.Category;
import gift.model.CategoryRepository;
import io.cucumber.java.ko.그러면;
import io.cucumber.java.ko.만일;
import io.cucumber.java.ko.조건;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;

public class ProductStepDefinitions {

    @Autowired
    private ScenarioContext context;

    @Autowired
    private CategoryRepository categoryRepository;

    @조건("{string} 카테고리가 존재한다")
    public void 카테고리가_존재한다(String categoryName) {
        Category category = categoryRepository.save(new Category(categoryName));
        context.set("categoryId", category.getId());
    }

    @만일("{string} 카테고리에 {string} 상품을 생성한다")
    public void 카테고리에_상품을_생성한다(String categoryName, String productName) {
        Long categoryId = context.get("categoryId", Long.class);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"" + productName + "\", \"price\": 10000, \"imageUrl\": \"http://image.url\", \"categoryId\": " + categoryId + "}")
                .when()
                .post("/api/products");

        context.set("lastResponse", response);
    }

    @만일("존재하지 않는 카테고리로 상품을 생성한다")
    public void 존재하지_않는_카테고리로_상품을_생성한다() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"테스트상품\", \"price\": 10000, \"imageUrl\": \"http://image.url\", \"categoryId\": 9999}")
                .when()
                .post("/api/products");

        context.set("lastResponse", response);
    }

    @그러면("상품 생성이 성공한다")
    public void 상품_생성이_성공한다() {
        Response response = context.get("lastResponse", Response.class);
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @그러면("상품 생성이 실패한다")
    public void 상품_생성이_실패한다() {
        Response response = context.get("lastResponse", Response.class);
        assertThat(response.statusCode()).isEqualTo(500);
    }

    @만일("상품 목록을 조회한다")
    public void 상품_목록을_조회한다() {
        Response response = RestAssured.given()
                .when()
                .get("/api/products");

        context.set("lastResponse", response);
    }

    @그러면("상품 목록에 {string}이 포함되어 있다")
    public void 상품_목록에_포함되어_있다(String productName) {
        Response response = context.get("lastResponse", Response.class);
        response.then()
                .statusCode(200)
                .body("name", hasItem(productName));
    }

    @그러면("상품 목록이 비어있다")
    public void 상품_목록이_비어있다() {
        Response response = context.get("lastResponse", Response.class);
        response.then()
                .statusCode(200)
                .body("$", empty());
    }
}
