package database


import java.util.UUID
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala._
import com.github.nscala_time.time.Imports._


object DB {

  RegisterJodaLocalDateTimeConversionHelpers()

}
