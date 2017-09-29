package cd.go.authorization.google.models;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TokenInfoTest {

    @Test
    public void shouldDeserializeJSON() throws Exception {
        final TokenInfo tokenInfo = TokenInfo.fromJSON("{\n" +
                "  \"access_token\": \"31239032-xycs.xddasdasdasda\",\n" +
                "  \"expires_in\": 7200,\n" +
                "  \"token_type\": \"foo-type\",\n" +
                "  \"refresh_token\": \"refresh-xysaddasdjlascdas\"\n" +
                "}");

        assertThat(tokenInfo.accessToken(), is("31239032-xycs.xddasdasdasda"));
        assertThat(tokenInfo.expiresIn(), is(7200L));
        assertThat(tokenInfo.tokenType(), is("foo-type"));
        assertThat(tokenInfo.refreshToken(), is("refresh-xysaddasdjlascdas"));
    }

    @Test
    public void shouldSerializeToJSON() throws Exception {
        final TokenInfo tokenInfo = new TokenInfo("31239032-xycs.xddasdasdasda", 7200, "foo-type", "refresh-xysaddasdjlascdas");

        final String expectedJSON = "{\n" +
                "  \"access_token\": \"31239032-xycs.xddasdasdasda\",\n" +
                "  \"expires_in\": 7200,\n" +
                "  \"token_type\": \"foo-type\",\n" +
                "  \"refresh_token\": \"refresh-xysaddasdjlascdas\"\n" +
                "}";

        JSONAssert.assertEquals(expectedJSON, tokenInfo.toJSON(), true);
    }
}