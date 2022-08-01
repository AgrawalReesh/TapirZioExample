ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val ZHTTPVersion = "2.0.0-RC7"
val ZIOVersion   = "2.0.0-RC5"
val ZIOPrelude   = "1.0.0-RC15"

lazy val root = (project in file("."))
  .settings(
    name := "xq-cash-pool-app"
  )

libraryDependencies ++= Seq(
  "dev.zio"                     %% "zio"                     % ZIOVersion,
  "io.d11"                      %% "zhttp"                   % ZHTTPVersion,
  "io.d11"                      %% "zhttp-test"              % ZHTTPVersion % Test,
  "dev.zio"                     %% "zio-prelude"             % ZIOPrelude,
  "dev.zio"                     %% "zio-json"                % "0.3.0-RC7",
  "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server"   % "1.0.3",
  "com.softwaremill.sttp.tapir" %% "tapir-json-zio"          % "1.0.3",
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.0.3"
)
