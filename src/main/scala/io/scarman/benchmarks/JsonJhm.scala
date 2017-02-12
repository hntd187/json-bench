package io.scarman.benchmarks

import java.io.{BufferedReader, File, FileInputStream, FileReader}
import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
abstract class JmhBenchmarks(name: String) {
  private val path: String = s"src/main/resources/$name"
  private def load(path: String): String = {
    val file  = new File(path)
    val bytes = new Array[Byte](file.length.toInt)
    val fis   = new FileInputStream(file)
    fis.read(bytes)
    new String(bytes, "UTF-8")
  }

  private def reader(path: String): FileReader       = new FileReader(new File(path))
  private def buffered(path: String): BufferedReader = new BufferedReader(new FileReader(new File(path)))

  @Benchmark def argonautParse(): Either[String, argonaut.Json]               = argonaut.Parse.parse(load(path))
  @Benchmark def boonParse(): AnyRef                                          = io.advantageous.boon.json.JsonFactory.fromJson(load(path))
  @Benchmark def circeParse(): Either[io.circe.ParsingFailure, io.circe.Json] = io.circe.parser.parse(load(path))
  @Benchmark def gsonParse(): com.google.gson.JsonElement                     = new com.google.gson.JsonParser().parse(buffered(path))
  @Benchmark def json4sJacksonParse(): org.json4s.JValue                      = org.json4s.jackson.JsonMethods.parse(load(path))
  @Benchmark def json4sNativeParse(): org.json4s.JValue                       = org.json4s.native.JsonMethods.parse(load(path))
  @Benchmark def jawnParse(): jawn.ast.JValue                                 = jawn.ast.JParser.parseFromString(load(path)).get
  @Benchmark def minimalJsonParse(): com.eclipsesource.json.JsonValue         = com.eclipsesource.json.Json.parse(load(path))
  @Benchmark def parboiledParse()                                             = new ParboiledParser(load(path)).Json.run().get
  @Benchmark def playParse(): play.api.libs.json.JsValue                      = play.api.libs.json.Json.parse(load(path))
  @Benchmark def rojomaParse(): com.rojoma.json.ast.JValue                    = com.rojoma.json.io.JsonReader.fromReader(reader(path))
  @Benchmark def rojomaV3Parse(): com.rojoma.json.v3.ast.JValue               = com.rojoma.json.v3.io.JsonReader.fromReader(reader(path), blockSize = 100000)
  @Benchmark def sprayParse(): spray.json.JsValue                             = spray.json.JsonParser(load(path))
  @Benchmark def jacksonParse(): com.fasterxml.jackson.databind.JsonNode = {
    new com.fasterxml.jackson.databind.ObjectMapper().readValue(new File(path), classOf[com.fasterxml.jackson.databind.JsonNode])
  }
}

//class Qux2Bench extends JmhBenchmarks("qux2.json")
class Bla25Bench extends JmhBenchmarks("bla25.json")
//class Companies   extends JmhBenchmarks("companies.json")
class Ugh10kBench extends JmhBenchmarks("ugh10k.json")
//class CountriesBench extends JmhBenchmarks("countries.geo.json")
