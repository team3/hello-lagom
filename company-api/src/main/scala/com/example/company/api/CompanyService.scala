package com.example.company.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import play.api.libs.json.{Format, Json}

trait CompanyService extends Service {

  def getCompany(id: String): ServiceCall[NotUsed, Company]

  def saveCompany(): ServiceCall[Company, NotUsed]

  override def descriptor: Descriptor = {
    import Service._

    named("company").withCalls(
      restCall(Method.GET, "/companies/:id", getCompany _),
      restCall(Method.POST, "/companies", saveCompany _)
    ).withAutoAcl(true)
  }
}

case class Company(name: String)

object Company {
  implicit val format: Format[Company] = Json.format[Company]
}
