package net.savantly.horus.webapp.integtests.smoke;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.services.xactn.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import net.savantly.horus.modules.content.dom.contentType.ContentType;
import net.savantly.horus.modules.content.dom.contentType.ContentTypes;
import net.savantly.horus.webapp.integtests.ApplicationIntegTestAbstract;

@Transactional
class Smoke_IntegTest extends ApplicationIntegTestAbstract {

    @Inject ContentTypes menu;
    @Inject TransactionService transactionService;

    @Test
    void happy_case() {

        // when
        List<ContentType> all = wrap(menu).listAll();

        // then
        assertThat(all).isEmpty();


        // when
        final ContentType fred = wrap(menu).create("fred");
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(1);
        assertThat(all).contains(fred);


        // when
        final ContentType bill = wrap(menu).create("Bill");
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(2);
        assertThat(all).contains(fred, bill);

    }

}

