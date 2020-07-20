package net.savantly.horus.webapp.application.fixture;

import org.springframework.stereotype.Service;

import net.savantly.horus.webapp.application.fixture.scenarios.DomainAppDemo;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.testing.fixtures.applib.fixturescripts.FixtureScripts;
import org.apache.isis.testing.fixtures.applib.fixturespec.FixtureScriptsSpecification;
import org.apache.isis.testing.fixtures.applib.fixturespec.FixtureScriptsSpecificationProvider;

/**
 * Specifies where to find fixtures, and other settings.
 */
@Service
public class DomainAppFixtureScriptsSpecificationProvider implements FixtureScriptsSpecificationProvider {
    @Override
    public FixtureScriptsSpecification getSpecification() {
        return FixtureScriptsSpecification
                .builder(DomainAppFixtureScriptsSpecificationProvider.class)
                .with(FixtureScripts.MultipleExecutionStrategy.EXECUTE)
                .withRunScriptDefault(DomainAppDemo.class)
                .withRecreate(DomainAppDemo.class)
                .build();
    }
}
