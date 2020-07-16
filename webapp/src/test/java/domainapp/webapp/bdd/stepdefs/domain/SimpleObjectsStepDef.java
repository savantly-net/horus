package domainapp.webapp.bdd.stepdefs.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.isis.applib.services.wrapper.WrapperFactory;

import domainapp.modules.content.contentType.ContentType;
import domainapp.modules.content.contentType.ContentTypes;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class SimpleObjectsStepDef {

    @Given("^there (?:is|are).* (\\d+) simple object[s]?$")
    public void there_are_N_simple_objects(int n) {
        final List<ContentType> list = wrap(simpleObjects).listAllContentTypes();
        assertThat(list.size(), is(n));
    }

    @When("^.*create (?:a|another) .*simple object$")
    public void create_a_simple_object() {
        wrap(simpleObjects).createContentType(UUID.randomUUID().toString());
    }

    <T> T wrap(T domainObject) {
        return wrapperFactory.wrap(domainObject);
    }

    @Inject protected ContentTypes simpleObjects;
    @Inject protected WrapperFactory wrapperFactory;

}
