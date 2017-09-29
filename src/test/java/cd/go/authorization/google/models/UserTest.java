package cd.go.authorization.google.models;

import cd.go.authorization.google.GoogleUser;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static cd.go.authorization.google.utils.Util.GSON;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UserTest {

    @Test
    public void shouldSerializeToJSON() throws Exception {
        final User user = new User("foo", "bar", "baz");
        final String expectedJSON = "{\"username\":\"foo\",\"display_name\":\"bar\",\"email\":\"baz\"}";

        JSONAssert.assertEquals(expectedJSON, GSON.toJson(user), true);
    }

    @Test
    public void shouldCreateUserFromGoogleUser() throws Exception {
        final GoogleUser googleUser = new GoogleUser("foo@bar.com", "Foo Bar");
        final User user = new User(googleUser);

        assertThat(user.username(), is("foo@bar.com"));
        assertThat(user.displayName(), is("Foo Bar"));
        assertThat(user.emailId(), is("foo@bar.com"));
    }
}