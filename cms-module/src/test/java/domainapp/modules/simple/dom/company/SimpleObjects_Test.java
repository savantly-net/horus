package domainapp.modules.simple.dom.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import domainapp.modules.simple.dom.company.Company;
import domainapp.modules.simple.dom.company.Companies;

import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.core.commons.internal.collections._Lists;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleObjects_Test {

    @Mock RepositoryService mockRepositoryService;
    @Mock IsisJdoSupport_v3_2 mockIsisJdoSupport_v3_2;

    Companies objects;

    @BeforeEach
    public void setUp() {
        objects = new Companies(mockRepositoryService, mockIsisJdoSupport_v3_2);
    }

    @Nested
    class create {

        @Test
        void happyCase() {

            // given
            final String someName = "Foobar";

            // expect
            when(mockRepositoryService.persist(
                    argThat((ArgumentMatcher<Company>) simpleObject -> Objects.equals(simpleObject.getName(), someName)))
            ).then((Answer<Company>) invocation -> invocation.getArgument(0));

            // when
            final Company obj = objects.create(someName);

            // then
            assertThat(obj).isNotNull();
            assertThat(obj.getName()).isEqualTo(someName);
        }
    }

    @Nested
    class ListAll {

        @Test
        void happyCase() {

            // given
            final List<Company> all = new ArrayList<>();

            // expecting
            when(mockRepositoryService.allInstances(Company.class))
                .thenReturn(all);

            // when
            final List<Company> list = objects.listAll();

            // then
            assertThat(list).isEqualTo(all);
        }
    }
}
