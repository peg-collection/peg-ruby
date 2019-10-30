organization := "com.github.peg_collection"

name := "peg_ruby"

def Scala211 = "2.11.12"
def Scala212 = "2.12.10"
def Scala213 = "2.13.1"
def CurrentScala = Scala213

scalaVersion := CurrentScala

crossScalaVersions := Seq(Scala211, Scala212, Scala213)

publishMavenStyle := true

val scaladocBranch = settingKey[String]("branch name for scaladoc -doc-source-url")

scaladocBranch := "master"

scalacOptions in (Compile, doc) ++= { Seq(
  "-sourcepath", baseDirectory.value.getAbsolutePath,
  "-doc-source-url", s"https://github.com/peg-collection/peg-ruby/tree/${scaladocBranch.value}€{FILE_PATH}.scala"
)}

scalacOptions ++= {
  Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")
}


libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
  "org.wvlet.airframe" %% "airspec" % "19.10.0" % "test"
)

testFrameworks += new TestFramework("wvlet.airspec.Framework")

assemblyJarName in assembly := "peg-ruby.jar"

mainClass in assembly := Some("com.github.peg_collection.peg_ruby.RubyParser")

initialCommands in console += {
  Iterator(
    "com.github.peg_collection.peg_ruby._"
  ).map("import "+).mkString("\n")
}

pomExtra := (
  <url>https://github.com/peg-collection/peg-ruby</url>
  <licenses>
    <license>
      <name>The MIT License</name>
      <url>http://www.opensource.org/licenses/MIT</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:peg-collection/peg-ruby.git</url>
    <connection>scm:git:git@github.com:peg-collection/peg-ruby.git</connection>
  </scm>
  <developers>
    <developer>
      <id>kmizu</id>
      <name>Kota Mizushima</name>
      <url>https://github.com/kmizu</url>
    </developer>
  </developers>
)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.endsWith("-SNAPSHOT"))
    Some("snapshots" at nexus+"content/repositories/snapshots")
  else
    Some("releases" at nexus+"service/local/staging/deploy/maven2")
}

credentials ++= {
  val sonatype = ("Sonatype Nexus Repository Manager", "oss.sonatype.org")
  def loadMavenCredentials(file: java.io.File) : Seq[Credentials] = {
    xml.XML.loadFile(file) \ "servers" \ "server" map (s => {
      val host = (s \ "id").text
      val realm = if (host == sonatype._2) sonatype._1 else "Unknown"
      Credentials(realm, host, (s \ "username").text, (s \ "password").text)
    })
  }
  val ivyCredentials   = Path.userHome / ".ivy2" / ".credentials"
  val mavenCredentials = Path.userHome / ".m2"   / "settings.xml"
  (ivyCredentials.asFile, mavenCredentials.asFile) match {
    case (ivy, _) if ivy.canRead => Credentials(ivy) :: Nil
    case (_, mvn) if mvn.canRead => loadMavenCredentials(mvn)
    case _ => Nil
  }
}
