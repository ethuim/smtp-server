# smtp-server installation

## Install as Windows Service

1. Update smtp-server.xml configuration file. Use the absolute file paths to java and smtp-server.jar.
2. Run `smtp-server.exe install` to install the service (As system administrator)
3. Run `smtp-server.exe start` to start the service

## Startup Arguments

Edit the startup arguments in smtp-server.xml, for example:

`<arguments>-Xrs -Xmx256m -jar "C:\Program Files\smtp-server\smtp-server.jar" --smtpserver.port=26 --smtpserver.directory=C:/tmp/incoming</arguments>`

> Note; `-Xrs` stands for “Reduced Signal”, it will tell Java to ignore the logoff signal and continue to run after the user logs out.

See [application.properties](./src/main/resources/application.properties) file for defaults:

``` properties
smtpserver.port=26
smtpserver.directory=C:/tmp/incoming
```

## Build using Maven

If mvn is not installed, you can use included Maven.

Windows:
`mvnw.cmd package -Dmaven.test.skip=true`

Unix:
`mvnw package -Dmaven.test.skip=true`

> Note: It may be useful to skip tests if port is in use.

## Download winsw

1. To update smtp-server.exe, download winsw from here:
https://github.com/winsw/winsw
2. Rename winsw.exe to smtp-server.exe
