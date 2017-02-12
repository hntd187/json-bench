name := "json-bench"
version := "1.0"
scalaVersion := "2.11.8"
scalacOptions := Seq("-optimise", "-deprecation", "-unchecked")

lazy val jackson = "2.8.6"
lazy val json4s  = "3.5.0"
lazy val jawn    = "0.10.4"
lazy val circe   = "0.7.0"

libraryDependencies ++= Seq(
  "io.circe"                       %% "circe-core"          % circe,
  "io.circe"                       %% "circe-generic"       % circe,
  "io.circe"                       %% "circe-parser"        % circe,
  "io.argonaut"                    %% "argonaut"            % "6.2-RC2",
  "org.spire-math"                 %% "jawn-ast"            % jawn,
  "org.spire-math"                 %% "jawn-parser"         % jawn,
  "org.json4s"                     %% "json4s-native"       % json4s,
  "org.json4s"                     %% "json4s-jackson"      % json4s,
  "com.typesafe.play"              %% "play-json"           % "2.5.12",
  "com.rojoma"                     %% "rojoma-json"         % "2.4.3",
  "com.rojoma"                     %% "rojoma-json-v3"      % "3.7.0",
  "io.spray"                       %% "spray-json"          % "1.3.3",
  "org.parboiled"                  %% "parboiled"           % "2.1.4",
  "com.fasterxml.jackson.core"     %  "jackson-annotations" % jackson,
  "com.fasterxml.jackson.core"     %  "jackson-core"        % jackson,
  "com.fasterxml.jackson.core"     %  "jackson-databind"    % jackson,
  "com.google.code.gson"           %  "gson"                % "2.8.0",
  "com.eclipsesource.minimal-json" %  "minimal-json"        % "0.9.4",
  "io.advantageous.boon"           % "boon-json"            % "0.6.6"
)

fork in run := true
javaOptions in run := Seq(
  "-server",
  "-Xmx10G",
  "-Xms1G",
  "-XX:+UseConcMarkSweepGC",
  "-XX:+UseParNewGC",
  "-XX:ReservedCodeCacheSize=256m",
  "-XX:+TieredCompilation",
  "-XX:+CMSClassUnloadingEnabled",
  "-XX:+UseFastAccessorMethods",
  "-XX:+OptimizeStringConcat"
)

lazy val root = project.in(file("."))
  .settings(name := "Benches")
  .enablePlugins(JmhPlugin)

sourceDirectory     in Jmh := (sourceDirectory     in Compile).value
classDirectory      in Jmh := (classDirectory      in Compile).value
dependencyClasspath in Jmh := (dependencyClasspath in Compile).value

compile in Jmh <<= (compile in Jmh) dependsOn (compile      in Compile)
run     in Jmh <<= (run     in Jmh) dependsOn (Keys.compile in Jmh)
