lazy val akkaHttpVersion = "10.2.8"
lazy val akkaVersion    = "2.6.18"

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

lazy val root = (project in file(".")).
  settings(
      organization    := "org.bargsten",
      scalaVersion    := "2.13.4",
    name := "placeholder-generator",
    libraryDependencies ++= testDependencies,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",

    )
  )

lazy val testDependencies = Seq(

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.1.4"         % Test
      )

// "org.typelevel"               %% "cats-core"                  % "2.7.0",
// "org.slf4j"                    % "slf4j-api"                  % "1.7.32",
// "com.softwaremill.sttp.model" %% "core"                       % "1.4.18",
// "commons-io"                   % "commons-io"                 % "2.11.0",

/*
lazy val testDependencies = Seq(
  "org.scalatest"     %% "scalatest-flatspec"       % "3.2.9"    % Test,
  "org.scalatest"     %% "scalatest-shouldmatchers" % "3.2.9"    % Test,
//  "org.mockito"        % "mockito-core"             % "4.1.0"    % Test,
  "org.scalatestplus" %% "scalatestplus-mockito"    % "1.0.0-M2" % Test,
  "org.mockito"       %% "mockito-scala-scalatest"  % "1.16.46"  % Test,
  "org.mockito"       %% "mockito-scala"            % "1.16.46"  % Test,
)
*/
