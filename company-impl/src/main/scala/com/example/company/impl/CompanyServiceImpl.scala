package com.example.company.impl

import java.util.UUID

import akka.NotUsed
import com.example.company.api.{Company, CompanyService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.global

class CompanyServiceImpl(val persistentEntityRegistry: PersistentEntityRegistry) extends CompanyService {
  override def getCompany(id: String): ServiceCall[NotUsed, Company] = {
    ServiceCall { _ =>
      val entityRef = persistentEntityRegistry.refFor[CompanyEntity](id)

      entityRef.ask(GetCompany(id)).map {
        case Some(company) => company
        case None => throw NotFound("Company was not found")
      }
    }
  }

  override def saveCompany(): ServiceCall[Company, Company] = {
    ServiceCall { company: Company =>
      Logger.debug(company.name)

      val companyId = UUID.randomUUID().toString

      val entityRef = persistentEntityRegistry.refFor[CompanyEntity](companyId)
      val newCompany: Company = company.copy(id = Some(companyId))

      entityRef.ask(CreateCompany(newCompany)).map { _ => newCompany }
    }
  }
}
