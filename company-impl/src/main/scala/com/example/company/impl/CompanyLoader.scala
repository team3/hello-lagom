package com.example.company.impl

import com.example.company.api.CompanyService
import com.lightbend.lagom.scaladsl.api.{Descriptor, ServiceLocator}
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class CompanyLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new CompanyServiceApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication = {
    new CompanyServiceApplication(context) with LagomDevModeComponents
  }

  override def describeService: Option[Descriptor] = {
    Some(readDescriptor[CompanyService])
  }
}

abstract class CompanyServiceApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents
    with LagomKafkaComponents
    with CassandraPersistenceComponents {

  override lazy val lagomServer: LagomServer = serverFor[CompanyService](wire[CompanyServiceImpl])

  override lazy val jsonSerializerRegistry = CompanyServiceSerializerRegistry

}

