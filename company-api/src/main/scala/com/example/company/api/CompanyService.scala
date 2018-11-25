package com.example.company.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import play.api.libs.json.{Format, Json}

trait CompanyService extends Service {

  def getCompany(id: Company#Id): ServiceCall[NotUsed, Company]

  def saveCompany(): ServiceCall[Company, Company]

  override def descriptor: Descriptor = {
    import Service._

    named("company").withCalls(
      restCall(Method.GET, "/companies/:id", getCompany _),
      restCall(Method.POST, "/companies", saveCompany _)
    ).withAutoAcl(true)
  }
}

case class Company(id: Option[Company#Id], name: String) {
  type Id = String
}

object Company {
  implicit val format: Format[Company] = Json.format[Company]
}
