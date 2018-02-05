package com.example.company.impl

import java.util.UUID

import com.example.company.api.{Company, CompanyService}
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import com.lightbend.lagom.scaladsl.testkit.ServiceTest.TestServer
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

class CompanyServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {
  lazy val server: TestServer[CompanyServiceApplication with LocalServiceLocator] = ServiceTest.startServer(ServiceTest.defaultSetup) { ctx =>
    new CompanyServiceApplication(ctx) with LocalServiceLocator
  }

  lazy val client: CompanyService = server.serviceClient.implement[CompanyService]

  override protected def afterAll(): Unit = {
    server.stop()
  }

  override protected def beforeAll(): Unit = server

  "The CompanyService" should {
    "should get company" in {
      client.getCompany(UUID.randomUUID().toString).invoke().map { answer =>
        answer should ===(Company("Test"))
      }
    }
  }
}
