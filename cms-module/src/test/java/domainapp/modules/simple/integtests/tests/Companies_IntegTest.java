package domainapp.modules.simple.integtests.tests;

import java.util.List;

import javax.inject.Inject;
import javax.jdo.JDODataStoreException;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import org.apache.isis.testing.integtestsupport.applib.ThrowableMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domainapp.modules.simple.dom.company.Company;
import domainapp.modules.simple.fixture.company.Company_persona;
import domainapp.modules.simple.dom.company.Companies;
import domainapp.modules.simple.integtests.SimpleModuleIntegTestAbstract;

@Transactional
public class Companies_IntegTest extends SimpleModuleIntegTestAbstract {

    @Inject
    Companies menu;

    public static class listAll extends Companies_IntegTest {

        @Test
        public void happyCase() {

            // given
            fixtureScripts.run(new Company_persona.PersistAll());
            transactionService.flushTransaction();

            // when
            final List<Company> all = wrap(menu).listAll();

            // then
            assertThat(all).hasSize(Company_persona.values().length);
        }

        @Test
        public void whenNone() {

            // when
            final List<Company> all = wrap(menu).listAll();

            // then
            assertThat(all).hasSize(0);
        }
    }

    public static class create extends Companies_IntegTest {

        @Test
        public void happyCase() {

            wrap(menu).create("Faz");

            // then
            final List<Company> all = wrap(menu).listAll();
            assertThat(all).hasSize(1);
        }

        @Test
        public void whenAlreadyExists() {

            // given
            fixtureScripts.runPersona(Company_persona.FOUNDATION);
            transactionService.flushTransaction();

            // expect
            Throwable cause = assertThrows(Throwable.class, ()->{

                // when
                wrap(menu).create("Foundation Repair Company");
                transactionService.flushTransaction();

            });

            // also expect
            MatcherAssert.assertThat(cause, 
                    ThrowableMatchers.causedBy(JDODataStoreException.class));

        }

    }


}