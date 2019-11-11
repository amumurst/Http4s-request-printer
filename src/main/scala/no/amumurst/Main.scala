package no.amumurst

import cats.effect.{ExitCode, IO, IOApp}
import io.circe.Json
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger
import org.http4s.{HttpApp, Request, Response}

object Main extends IOApp with Http4sDsl[IO] {
  def requestToResponseMapper(request: Request[IO]): IO[Response[IO]] =
    Ok(request.as[String].map { bodyString =>
      Json.obj(
        "uri"    -> request.uri.renderString.asJson,
        "method" -> request.method.name.asJson,
        "headers" -> Json.arr(
          request.headers.toList.map(_.toString().asJson): _*),
        "body"          -> bodyString.asJson,
        "remoteAddress" -> request.remoteAddr.asJson,
        "remotePort"    -> request.remotePort.asJson,
        "remoteHost"    -> request.remoteHost.asJson,
      )
    })

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(Logger.httpApp(logHeaders = true, logBody = true)(
        HttpApp[IO](requestToResponseMapper)))
      .resource
      .use(_ => IO.never)
      .map(_ => ExitCode.Success)
}
