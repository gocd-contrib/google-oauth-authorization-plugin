package cd.go.authorization.google;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GoogleUserTest {

    @Test
    public void shouldDeserializeJSON() throws Exception {
        final GoogleUser googleUser = GoogleUser.fromJSON("{\n" +
                " \"id\": \"10536933078\",\n" +
                " \"email\": \"foo@example.com\",\n" +
                " \"verified_email\": true,\n" +
                " \"name\": \"Foo Bar\",\n" +
                " \"given_name\": \"Bar\",\n" +
                " \"family_name\": \"Foo\",\n" +
                " \"picture\": \"xfeowjflsad.jpg\",\n" +
                " \"locale\": \"en\",\n" +
                " \"hd\": \"example.com\"\n" +
                "}");

        assertThat(googleUser.getId(), is("10536933078"));
        assertThat(googleUser.getEmail(), is("foo@example.com"));
        assertThat(googleUser.isVerifiedEmail(), is(true));
        assertThat(googleUser.getName(), is("Foo Bar"));
        assertThat(googleUser.getGivenName(), is("Bar"));
        assertThat(googleUser.getFamilyName(), is("Foo"));
        assertThat(googleUser.getPicture(), is("xfeowjflsad.jpg"));
        assertThat(googleUser.getLocale(), is("en"));
        assertThat(googleUser.getHd(), is("example.com"));
    }

    @Test
    public void shouldExtractHdFromEmailWhenNotPresent() throws Exception {
        final GoogleUser googleUser = GoogleUser.fromJSON("{\n" +
                " \"id\": \"10536933078\",\n" +
                " \"email\": \"foo@gmail.com\",\n" +
                " \"verified_email\": true,\n" +
                " \"name\": \"Foo Bar\",\n" +
                " \"given_name\": \"Bar\",\n" +
                " \"family_name\": \"Foo\",\n" +
                " \"picture\": \"xfeowjflsad.jpg\",\n" +
                " \"locale\": \"en\"\n" +
                "}");

        assertThat(googleUser.getHd(), is("gmail.com"));
    }
}