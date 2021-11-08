import sbt._
import Keys._

updateOptions := updateOptions.value.withCachedResolution(false)
//assemblySettings

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

overrideBuildResolvers := true

assemblyMergeStrategy in assembly := {
  case entry =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    if (oldStrategy(entry) == MergeStrategy.deduplicate) MergeStrategy.first
    else oldStrategy(entry)
}

// removes all jar mappings i
//mappings in Universal := {
//  // universalMappings: Seq[(File,String)]
//  val universalMappings = (mappings in Universal).value
//  val fatJar = (assembly in Compile).value
//  // removing means filtering
//  val filtered = universalMappings filter {
//    case (file, name) =>  ! name.endsWith(".jar")
//  }
//  // add the fat jar
//  filtered :+ (fatJar -> ("lib/" + fatJar.getName))
//}

artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.withClassifier(Some("assembly"))
}
addArtifact(artifact in (Compile, assembly), assembly)

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.common.**" -> "repackaged.com.google.common.@1").inAll,
  ShadeRule.rename("com.google.protobuf.**" -> "repackaged.com.google.protobuf.@1").inAll,
)