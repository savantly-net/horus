package net.savantly.horus.webapp.unittests.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import javax.jdo.annotations.PersistenceCapable;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.ViewModel;
import org.apache.isis.applib.annotation.ViewModelLayout;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import net.savantly.horus.modules.content.ContentModule;
import net.savantly.horus.webapp.SimpleApp;

@AnalyzeClasses(
        packagesOf = {ContentModule.class, SimpleApp.class},
        importOptions = {
                ImportOption.DoNotIncludeTests.class
        })
public class ArchitectureTests {

    @ArchTest
    static ArchRule package_dependencies =
            layeredArchitecture()
                    .layer("content module").definedBy("net.savantly.horus.modules.content..")
                    .layer("webapp").definedBy("net.savantly.horus.webapp..")
            .whereLayer("content module").mayOnlyBeAccessedByLayers("webapp");

    @ArchTest
    static ArchRule classes_annotated_with_PersistenceCapable_are_also_annotated_with_DomainObject =
            classes()
                   .that().areAnnotatedWith(PersistenceCapable.class)
            .should().beAnnotatedWith(DomainObject.class);

    @ArchTest
    static ArchRule classes_annotated_with_PersistenceCapable_are_also_annotated_with_XmlJavaTypeAdapter =
            classes()
                   .that().areAnnotatedWith(PersistenceCapable.class)
            .should().beAnnotatedWith(XmlJavaTypeAdapter.class);

    @ArchTest
    static ArchRule classes_annotated_with_DomainObject_are_also_annotated_with_DomainObjectLayout =
            classes()
                   .that().areAnnotatedWith(DomainObject.class)
            .should().beAnnotatedWith(DomainObjectLayout.class);

    @ArchTest
    static ArchRule classes_annotated_with_ViewModel_are_also_annotated_with_ViewModelLayout =
            classes()
                   .that().areAnnotatedWith(ViewModel.class)
            .should().beAnnotatedWith(ViewModelLayout.class);

}