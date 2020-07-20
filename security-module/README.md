# Horus Security Module  

This is a simple module that provides defaults for Isis Secman extension  

Import the security module into your web app's config [`HorusSecurityModule.class`]  

```
@Configuration
@Import({
        IsisModuleCoreRuntimeServices.class,
        IsisModuleSecurityShiro.class,
        IsisModuleJdoDataNucleus5.class,
        IsisModuleViewerRestfulObjectsJaxrsResteasy4.class,
        IsisModuleViewerWicketViewer.class,

        IsisModuleTestingFixturesApplib.class,
        IsisModuleTestingH2ConsoleUi.class,

        IsisModuleExtExcelDownloadUi.class,
        IsisModuleExtFlywayImpl.class,
        
        // Security Extension
        HorusSecurityModule.class,
        
        //IsisModuleExtCorsImpl.class,

        ApplicationModule.class,

        // discoverable fixtures
        DomainAppDemo.class
})
@PropertySources({
        @PropertySource(IsisPresets.DebugDiscovery),
})
public class AppManifest {
}
```


And a shiro.ini file in `src/main/resources`  

Example file -  

```
[main]

authenticationStrategy=org.apache.isis.extensions.secman.shiro.AuthenticationStrategyForIsisModuleSecurityRealm
isisModuleSecurityRealm=org.apache.isis.extensions.secman.shiro.IsisModuleExtSecmanShiroRealm

securityManager.authenticator.authenticationStrategy = $authenticationStrategy
securityManager.realms = $isisModuleSecurityRealm

[users]
[roles]
```

Configure the initial username and password through the SpringBoot application properties/yml - [defaults shown below]  

```
horus:
  modules:
    security:
      admin-username: admin
      admin-password: password
```