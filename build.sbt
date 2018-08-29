lazy val name = "exampleScalaProject"

enablePlugins(JavaAppPackaging)

packageBin in Universal := {
  val originalName = (packageBin in Universal).value
  val (base, ext) = originalName.baseAndExt
  val newName = file(originalName.getParent) / (base + "_something_." + ext)
  IO.move(originalName, newName)
  newName
}