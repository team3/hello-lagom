package com.example.company.impl

import akka.NotUsed
import com.example.company.api.{Company, CompanyService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import play.api.Logger

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class CompanyServiceImpl extends CompanyService {
  override def getCompany(id: String): ServiceCall[NotUsed, Company] = {
    ServiceCall { _ => Future(Company("Test")) }
  }

  override def saveCompany(): ServiceCall[Company, NotUsed] = {
    ServiceCall { company: Company =>
      Logger.debug(company.name)

      Future.successful(NotUsed)
    }
  }
}
