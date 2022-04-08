name := "guessing-game"
version := "1.0"
scalaVersion := "3.1.1"

enablePlugins(JavaAppPackaging)

libraryDependencies ++= Seq(
	"dev.zio" %% "zio" % "2.0.0-RC4"
)

enablePlugins(DockerPlugin)
maintainer in Docker := "Kurt Fehlhauer"
packageSummary in Docker := "Guessing Game Application"
packageDescription := "Guessing Game Application"
