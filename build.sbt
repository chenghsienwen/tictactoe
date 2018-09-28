
val Http4sVersion = "0.19.0-M2"
val CirceVersion = "0.10.0"
val Specs2Version = "4.2.0"
val LogbackVersion = "1.2.3"

val circeDep = Seq(
  "io.circe" %% "circe-core"    % CirceVersion,
  "io.circe" %% "circe-parser"  % CirceVersion,
  "io.circe" %% "circe-generic" % CirceVersion
)

val http4sCirce = "org.http4s" %% "http4s-circe" % Http4sVersion

val logback = "ch.qos.logback"  %  "logback-classic" % LogbackVersion
val specs   = "org.specs2"      %% "specs2-core"     % Specs2Version % "test"

lazy val root = (project in file("."))
  .settings(
    organization := "scalasummerschool",
    name         := "tictactoe",
    version      := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.6",
  )
  .aggregate(server, client)

lazy val server = (project in file("server"))
  .settings(
    libraryDependencies ++= circeDep ++ Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-dsl"          % Http4sVersion,
      http4sCirce,
      logback,
      specs
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )
  .dependsOn(model)

lazy val client = (project in file("client"))
  .settings(
    libraryDependencies ++= circeDep ++ Seq(
      "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
      http4sCirce,
      logback,
      specs
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )
  .dependsOn(model)

lazy val model = (project in file("model"))
  .settings(
    libraryDependencies ++= circeDep,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )
