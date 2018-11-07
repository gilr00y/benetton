name := "Benetton"

organizationName := "com.expr"

scalaVersion := "2.12.6"

val kettleVersion = "7.0.0.0-25"

resolvers ++= Seq(
  "pentaho-releases" at "https://nexus.pentaho.org/content/groups/omni/",
  "swt-repo" at "https://swt-repo.googlecode.com/svn/repo/",
  "commons" at "https://mvnrepository.com/artifact/org.apache.commons/commons-vfs2"
)

libraryDependencies ++= Seq(
  "org.apache.commons"   % "commons-lang3"                      % "3.3.2"       % "provided",
  "org.apache.commons" % "commons-vfs2" % "2.0",
  "commons-httpclient"   % "commons-httpclient"                 % "3.1"         % "provided",
  "commons-vfs"          % "commons-vfs"                        % "1.0"         % "provided",
  "pentaho-kettle"       % "kettle-engine"                      % kettleVersion % "provided",
  "pentaho-kettle"       % "kettle-core"                        % kettleVersion % "provided",
  "pentaho-kettle"       % "kettle-ui-swt"                      % kettleVersion % "provided",
  "org.eclipse.swt"      % "org.eclipse.swt.win32.win32.x86_64" % "4.3"         % "provided"
)

libraryDependencies  ++= Seq(
  // other dependencies here
  "org.scalanlp" %% "breeze" % "0.13",
  // native libraries are not included by default. add this if you want them (as of 0.7)
  // native libraries greatly improve performance, but increase jar sizes.
  // It also packages various blas implementations, which have licenses that may or may not
  // be compatible with the Apache License. No GPL code, as best I know.
  "org.scalanlp" %% "breeze-natives" % "0.13",
  // the visualization library is distributed separately as well.
  // It depends on LGPL code.
  "org.scalanlp" %% "breeze-viz" % "0.13"
)

fork := true

libraryDependencies ++= Seq("org.scala-lang.modules" %% "scala-xml" % "1.1.1")

resolvers ++= Seq(
  // other resolvers here
  // if you want to use snapshot builds (currently 0.12-SNAPSHOT), use this.
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)