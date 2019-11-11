organization := "no.amumurst"
name := "requestprintserver"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl"          % "0.21.0-M5",
  "org.http4s" %% "http4s-blaze-server" % "0.21.0-M5",
  "org.http4s" %% "http4s-circe"        % "0.21.0-M5",
  "org.slf4j"  % "slf4j-simple"         % "1.7.26"
)

enablePlugins(DockerPlugin)
dockerfile in docker := {
  val artifact: File     = assembly.value
  val artifactTargetPath = s"/app/${artifact.name}"

  new Dockerfile {
    from("java")
    expose(8080)
    add(artifact, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
  }
}
imageNames in docker := Seq(
  ImageName(s"amumurst/${name.value}:latest")
)
