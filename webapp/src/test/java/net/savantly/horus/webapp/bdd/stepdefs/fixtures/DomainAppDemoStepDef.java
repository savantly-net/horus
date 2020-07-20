package net.savantly.horus.webapp.bdd.stepdefs.fixtures;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.isis.applib.annotation.OrderPrecedence;
import org.apache.isis.testing.fixtures.applib.fixturescripts.FixtureScripts;

import io.cucumber.java.Before;
import net.savantly.horus.webapp.application.fixture.scenarios.DomainAppDemo;

public class DomainAppDemoStepDef {

    @Inject private FixtureScripts fixtureScripts;

    @Before(value="@DomainAppDemo", order= OrderPrecedence.MIDPOINT)
    public void runDomainAppDemo() {
        fixtureScripts.runFixtureScript(new DomainAppDemo(), null); // <1>
    }

}
