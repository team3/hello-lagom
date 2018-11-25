package com.example.company.impl

import akka.Done
import com.example.company.api.Company
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger, PersistentEntity}
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}

import scala.collection.immutable.Seq

class CompanyEntity extends PersistentEntity {
  override type Command = CompanyCommand[_]
  override type Event = CompanyEvent
  override type State = Option[Company]

  override def initialState: Option[Company] = None

  override def behavior: Behavior = {
    Actions().onReadOnlyCommand[GetCompany, Option[Company]] {
      case (GetCompany(_), ctx, state) => ctx.reply(state)
    }.onCommand[CreateCompany, Done] {
      case (CreateCompany(company), ctx, _) =>
        ctx.thenPersist(CompanyCreated(company))(_ => ctx.reply(Done))
    }.onEvent {
      case (CompanyCreated(company), _) => Some(company)
    }
  }
}

/*================ Commands ======================*/
sealed trait CompanyCommand[R] extends ReplyType[R]

case class GetCompany(id: Company#Id) extends CompanyCommand[Option[Company]]

case class CreateCompany(company: Company) extends CompanyCommand[Done]

object GetCompany {
  implicit val format: Format[GetCompany] = Json.format
}

object CreateCompany {
  implicit val format: Format[CreateCompany] = Json.format
}

/*================ Events ======================*/
sealed trait CompanyEvent extends AggregateEvent[CompanyEvent] {
  override def aggregateTag: AggregateEventTagger[CompanyEvent] = CompanyEvent.Tag
}

object CompanyEvent {
  val Tag: AggregateEventTag[CompanyEvent] = AggregateEventTag[CompanyEvent]
}

case class CompanyCreated(company: Company) extends CompanyEvent

object CompanyCreated {
  implicit val format: Format[CompanyCreated] = Json.format
}

/*================ State ======================*/
case class CompanyState(name: String)

object CompanyState {
  // json format
  implicit val format: Format[CompanyState] = Json.format
}

object CompanyServiceSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[Company],
    JsonSerializer[CompanyState],
    JsonSerializer[CompanyCreated]
  )
}
